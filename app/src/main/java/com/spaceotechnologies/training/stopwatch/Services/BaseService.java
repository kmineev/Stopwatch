package com.spaceotechnologies.training.stopwatch.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.spaceotechnologies.training.stopwatch.activitys.MainActivity;

/**
 * Created by Kostez on 03.08.2016.
 */
public abstract class BaseService extends Service {

    protected String SAVED_IS_TIMER_RUNNING = "savedIsStopvatchTimer";
    public static final String BROADCAST_ACTION = "serviceBackBroadcast";
    public static final String CURRENT_PAGE = "currentPage";

    protected NotificationManager notificationManager;
    protected Resources res;
    protected NotificationCompat.Builder builder;

    protected static final int NOTIFY_ID = 102;
    private final long FREQUENCY = 100;
    private final int TICK_WHAT = 5;
    protected final int DEFAULT_TIME = 0;

    protected Binder binder;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            updateNotification();
            sendMessageDelayed(Message.obtain(this, TICK_WHAT), FREQUENCY);
        }
    };

    public void createNotification() {
        builder = setBuilder();
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        builder.setContentIntent(resultPendingIntent);
    }

    public abstract void updateNotification();

    @Override
    public IBinder onBind(Intent intent) {
        createNotification();
        return binder;
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    public void showNotification() {
        updateNotification();
        mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), FREQUENCY);
    }

    public void hideNotification() {
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFY_ID);
        }
        mHandler.removeMessages(TICK_WHAT);
    }

    protected abstract NotificationCompat.Builder setBuilder();

    public abstract void start();

    public abstract void pause();

    public abstract void reset();

    public abstract long getElapsedTime();

    public abstract String setText();

}