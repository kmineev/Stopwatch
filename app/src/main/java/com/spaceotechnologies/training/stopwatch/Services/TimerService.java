package com.spaceotechnologies.training.stopwatch.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.spaceotechnologies.training.stopwatch.applications.MyApplication;
import com.spaceotechnologies.training.stopwatch.base.Timer;
import com.spaceotechnologies.training.stopwatch.R;

import static com.spaceotechnologies.training.stopwatch.activitys.MainActivity.TIMER_NUMBER;
import static com.spaceotechnologies.training.stopwatch.applications.MyApplication.getPreferences;

public class TimerService extends BaseService {

    private final int START_VALUE_TIMER = 15000;
    private Timer timer;
    private boolean isFinalNotification = false;

    public class LocalBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(getResources().getString(R.string.log_app), "TimerService onCreate");

        if (getPreferences().getBoolean(getString(R.string.saved_is_timer_running), false)) {
            timer = new Timer(getPreferences().getLong(getString(R.string.saved_start_time_timer), DEFAULT_TIME));
            startTimerService();
        } else {
            timer = new Timer();
        }
        binder = new LocalBinder();
    }

    private void startTimerService() {
        createNotification();
        this.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(getResources().getString(R.string.log_app), "TimerService onStartCommand");
        return START_STICKY;
    }

    @Override
    protected NotificationCompat.Builder setBuilder() {
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_timer_24dp)
                .setContentTitle(MyApplication.getAppContext().getString(R.string.timernotifytitle))
                .setOngoing(true);
    }

    @Override
    public String setText() {
        return getFormattedLeftTime();
    }

    public void start() {
        Log.d(getResources().getString(R.string.log_app), "TimerService start");
        if (!isFinalNotification) {
            timer.start();
            showNotification();
        }
    }

    public void pause() {
        timer.pause();
        if (!isFinalNotification) {
            hideNotification();
        }
    }

    public void reset() {
        timer.reset();
        hideNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(getResources().getString(R.string.log_app), "TimerService onBind");

        if (isTimerRunning() || isTimeout()) {
            sendPageItemReceiver();
        }

        return super.onBind(intent);
    }

    private void sendPageItemReceiver() {
        Intent intent1 = new Intent(getResources().getString(R.string.broadcast_action));
        intent1.putExtra(getResources().getString(R.string.current_page), TIMER_NUMBER);
        sendBroadcast(intent1);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(getResources().getString(R.string.log_app), "TimerService onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(getResources().getString(R.string.log_app), "TimerService onTaskRemoved");

        SharedPreferences sharedPref = MyApplication.getPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(getString(R.string.saved_start_time_timer), timer.getStartTime());
        editor.putBoolean(getString(R.string.saved_is_timer_running), isTimerRunning());
        editor.commit();

        if (!isTimerRunning()) {
            stopSelf();
        }

        super.onTaskRemoved(rootIntent);
    }

    private boolean isTimeout = false;

    public boolean isTimeout() {
        return isTimeout;
    }

    public void updateNotification() {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setContentText(getFormattedLeftTime());

        if (getDifference() < 1000) {
            isTimeout = true;
            if (!isFinalNotification) {
                isFinalNotification = true;
                builder.setTicker(getResources().getString(R.string.timeout));
                builder.setAutoCancel(true);
                Notification notification = builder.build();
                notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
                notificationManager.notify(NOTIFY_ID, notification);
                pause();
            }
        } else {
            notificationManager.notify(NOTIFY_ID, builder.build());
        }
    }

    public long getElapsedTime() {
        return timer.getElapsedTime();
    }

    public String getFormattedLeftTime() {
        return formatLeftTime(getDifference());
    }

    private long getDifference() {
        return START_VALUE_TIMER - getElapsedTime();
    }

    public boolean isTimerRunning() {
        return timer.isRunning();
    }

    private String formatLeftTime(long now) {
        long seconds;
        StringBuilder sb = new StringBuilder();
        seconds = now / 1000;
        sb.append((seconds));
        return sb.toString();
    }
}
