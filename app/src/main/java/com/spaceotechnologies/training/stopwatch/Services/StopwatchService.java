package com.spaceotechnologies.training.stopwatch.Services;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.spaceotechnologies.training.stopwatch.Applications.MyApplication;
import com.spaceotechnologies.training.stopwatch.Base.Stopwatch;
import com.spaceotechnologies.training.stopwatch.R;

import static com.spaceotechnologies.training.stopwatch.Activitys.MainActivity.STOPWATCH_NUMBER;

/**
 * Created by Kostez on 21.07.2016.
 */
public class StopwatchService extends BaseService {

    private Stopwatch stopwatch;

    public class LocalBinder extends Binder {
        public StopwatchService getService() {
            return StopwatchService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(getResources().getString(R.string.log_app), "StopwatchService onCreate");

        sharedPref = MyApplication.getPreferences();

        if (sharedPref.getBoolean(getString(R.string.saved_is_stopwatch_running), false)) {
            stopwatch = new Stopwatch(sharedPref.getLong(getString(R.string.saved_start_time_stopwatch), DEFAULT_TIME));
            startStopwatchService();
        } else {
            stopwatch = new Stopwatch();
        }

        if(!sharedPref.getBoolean(getString(R.string.saved_is_stopwatch_running), false)
                && (!sharedPref.getBoolean(getString(R.string.saved_is_timer_running), false))) {
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
        Log.d(getResources().getString(R.string.log_app), "StopwatchService onStartCommand");
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
        Log.d(getResources().getString(R.string.log_app), "StopwatchService start");
        stopwatch.start();
        showNotification();
    }

    public void pause() {
        Log.d(getResources().getString(R.string.log_app), "StopwatchService pause");
        stopwatch.pause();
        hideNotification();
    }

    public void reset() {
        stopwatch.reset();
        hideNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(getResources().getString(R.string.log_app), "StopwatchService onBind");

        if (isStopwatchRunning()) {
            sendPageItemReceiver();
        }

        return super.onBind(intent);
    }

    private void sendPageItemReceiver() {
        Intent intent1 = new Intent(getResources().getString(R.string.broadcast_action));
        intent1.putExtra(getResources().getString(R.string.current_page), STOPWATCH_NUMBER);
        sendBroadcast(intent1);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(getResources().getString(R.string.log_app), "StopwatchService onUnbind");
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
        Log.d(getResources().getString(R.string.log_app), "StopwatchService onTaskRemoved");

        SharedPreferences sharedPref = MyApplication.getPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(getString(R.string.saved_start_time_stopwatch), stopwatch.getStartTime());
        editor.putBoolean(getString(R.string.saved_is_stopwatch_running), isStopwatchRunning());
        editor.commit();

        if (!isStopwatchRunning()) {
            stopSelf();
        }

        super.onTaskRemoved(rootIntent);
    }
}