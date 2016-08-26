package com.spaceotechnologies.training.stopwatch.services;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.applications.MyApplication;
import com.spaceotechnologies.training.stopwatch.base.Stopwatch;

import static com.spaceotechnologies.training.stopwatch.activitys.MainActivity.BROADCAST_ACTION;
import static com.spaceotechnologies.training.stopwatch.activitys.MainActivity.CURRENT_PAGE;
import static com.spaceotechnologies.training.stopwatch.adapters.TextPagerAdapter.STOPWATCH_NUMBER;
import static com.spaceotechnologies.training.stopwatch.applications.MyApplication.getPreferences;

/**
 * Created by Kostez on 21.07.2016.
 */
public class StopwatchService extends BaseService {

    private Stopwatch stopwatch;
    private static final String SAVED_START_TIME_STOPWATCH = "savedStartTimeStopwatch";
    private static final String SAVED_IS_STOPWATCH_RUNNING = "savedIsStopwatchRunning";

    public class LocalBinder extends Binder {
        public StopwatchService getService() {
            return StopwatchService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (getPreferences().getBoolean(SAVED_IS_STOPWATCH_RUNNING, false)) {
            stopwatch = new Stopwatch(getPreferences().getLong(SAVED_START_TIME_STOPWATCH, DEFAULT_TIME));
            startStopwatchService();
        } else {
            stopwatch = new Stopwatch();
        }

        if (!getPreferences().getBoolean(SAVED_IS_STOPWATCH_RUNNING, false)
                && (!getPreferences().getBoolean(SAVED_IS_TIMER_RUNNING, false))) {
            startStopwatchService();
        }
        binder = new LocalBinder();
    }

    private void startStopwatchService() {
        createNotification();
        this.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void updateNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder.setContentText(setText());
        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    @Override
    public NotificationCompat.Builder setBuilder() {
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stopwatch)
                .setContentTitle(MyApplication.getAppContext().getString(R.string.stopwatchnotifytitle))
                .setOngoing(true);
    }

    @Override
    public String setText() {
        return getFormattedElapsedTime();
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

    @Override
    public IBinder onBind(Intent intent) {

        if (isStopwatchRunning()) {
            sendPageItemReceiver();
        }

        return super.onBind(intent);
    }

    private void sendPageItemReceiver() {
        Intent intent1 = new Intent(BROADCAST_ACTION);
        intent1.putExtra(CURRENT_PAGE, STOPWATCH_NUMBER);
        sendBroadcast(intent1);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
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

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(SAVED_START_TIME_STOPWATCH, stopwatch.getStartTime());
        editor.putBoolean(SAVED_IS_STOPWATCH_RUNNING, isStopwatchRunning());
        editor.commit();

        if (!isStopwatchRunning()) {
            stopSelf();
        }

        super.onTaskRemoved(rootIntent);
    }
}