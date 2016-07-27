package com.spaceotechnologies.training.stopwatch.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.spaceotechnologies.training.stopwatch.Activitys.MainActivity;
import com.spaceotechnologies.training.stopwatch.Base.Timer;
import com.spaceotechnologies.training.stopwatch.R;

public class TimerService extends Service {

    private Timer timer;
    private NotificationCompat.Builder builder;
    private final int startValueTimer = 10000;
    private final long frequency = 100;
    private final int TICK_WHAT = 5;
    private LocalBinder binder = new LocalBinder();
    private boolean isFinalNotification = false;
    private Resources res;
    private boolean isTimeout = false;
    private NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public class LocalBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            updateNotification();
            sendMessageDelayed(Message.obtain(this, TICK_WHAT), frequency);
        }
    };

    public void createNotification() {
        Context context = getApplicationContext();
        res = context.getResources();

        builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_timer_24dp)
                        .setContentTitle(res.getString(R.string.timernotifytitle))
                        .setOngoing(true);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, 0);
        builder.setContentIntent(resultPendingIntent);

    }

    public boolean isTimeout() {
        return isTimeout;
    }

    public void updateNotification() {

        builder.setContentText(getFormattedLeftTime());

        if (getDifference() < 1000) {
            isTimeout = true;
            if (!isFinalNotification) {
                isFinalNotification = true;
                builder.setTicker(res.getString(R.string.timeout));
                builder.setAutoCancel(true);
                Notification notification = builder.build();
                notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
                notificationManager.notify(res.getInteger(R.integer.NOTIFY_ID), notification);
                pause();
            }
        } else {
            notificationManager.notify(res.getInteger(R.integer.NOTIFY_ID), builder.build());
        }
    }

    public void showNotification() {
        updateNotification();
        mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), frequency);
    }

    public void hideNotification() {
        notificationManager.cancel(res.getInteger(R.integer.NOTIFY_ID));
        mHandler.removeMessages(TICK_WHAT);
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

    public long getElapsedTime() {
        return timer.getElapsedTime();
    }

    public String getFormattedLeftTime() {
        return formatLeftTime(getDifference());
    }

    private long getDifference() {
        return startValueTimer - getElapsedTime();
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
