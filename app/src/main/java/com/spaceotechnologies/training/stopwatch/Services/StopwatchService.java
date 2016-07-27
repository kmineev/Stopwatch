package com.spaceotechnologies.training.stopwatch.Services;

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
import com.spaceotechnologies.training.stopwatch.Base.Stopwatch;
import com.spaceotechnologies.training.stopwatch.R;

/**
 * Created by Kostez on 21.07.2016.
 */
public class StopwatchService extends Service {

    public class LocalBinder extends Binder {
        public StopwatchService getService() {
            return StopwatchService.this;
        }
    }

    private Stopwatch stopwatch;
    private LocalBinder binder = new LocalBinder();
    NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private Resources res;

    private final long frequency = 100;
    private final int TICK_WHAT = 5;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            updateNotification();
            sendMessageDelayed(Message.obtain(this, TICK_WHAT), frequency);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        stopwatch = new Stopwatch();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void createNotification() {

        Context context = getApplicationContext();
        res = context.getResources();

        builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stopwatch)
                        .setContentTitle(res.getString(R.string.stopwatchnotifytitle))
                        .setOngoing(true);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, 0);
        builder.setContentIntent(resultPendingIntent);

    }

    public void updateNotification() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setContentText(getFormattedElapsedTime());
        mNotificationManager.notify(res.getInteger(R.integer.NOTIFY_ID), builder.build());
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
        stopwatch.start();
        showNotification();
    }

    public void pause() {
        stopwatch.pause();
        hideNotification();
    }

    public void reset() {
        stopwatch.reset();
        hideNotification();
    }

    public long getElapsedTime() {
        return stopwatch.getElapsedTime();
    }

    public String getFormattedElapsedTime() {
        return formatElapsedTime(getElapsedTime());
    }

    public boolean isStopwatchRunning() {
        return stopwatch.isRunning();
    }

    private String formatElapsedTime(long now) {
        long seconds;
        StringBuilder sb = new StringBuilder();
        seconds = now / 1000;
        sb.append((seconds));
        return sb.toString();
    }
}