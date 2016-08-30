package com.spaceotechnologies.training.stopwatch.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.applications.MyApplication;
import com.spaceotechnologies.training.stopwatch.base.Timer;

import static com.spaceotechnologies.training.stopwatch.adapters.TextPagerAdapter.TIMER_NUMBER;
import static com.spaceotechnologies.training.stopwatch.applications.MyApplication.getPreferences;

public class TimerService extends BaseService {

    private static final String SAVED_START_TIME_TIMER = "savedStartTimeTimer";

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

        if (getPreferences().getBoolean(SAVED_IS_TIMER_RUNNING, false)) {
            timer = new Timer(getPreferences().getLong(SAVED_START_TIME_TIMER, DEFAULT_TIME));
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

        if (isTimerRunning() || isTimeout()) {
            sendPageItemReceiver();
        }

        return super.onBind(intent);
    }

    private void sendPageItemReceiver() {
        Intent intent1 = new Intent(BROADCAST_ACTION);
        intent1.putExtra(CURRENT_PAGE, TIMER_NUMBER);
        sendBroadcast(intent1);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        SharedPreferences sharedPref = MyApplication.getPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(SAVED_START_TIME_TIMER, timer.getStartTime());
        editor.putBoolean(SAVED_IS_TIMER_RUNNING, isTimerRunning());
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
