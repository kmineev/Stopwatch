package com.spaceotechnologies.training.stopwatch.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.spaceotechnologies.training.stopwatch.Activitys.MainActivity;
import com.spaceotechnologies.training.stopwatch.Base.Stopwatch;
import com.spaceotechnologies.training.stopwatch.R;

/**
 * Created by Kostez on 21.07.2016.
 */
public class StopwatchService extends Service {

    private static final int NOTIFY_ID = 101;

    public class LocalBinder extends Binder {
        public StopwatchService getService() {
            return StopwatchService.this;
        }
    }

    private Stopwatch m_stopwatch;
    private LocalBinder m_binder = new LocalBinder();
    private NotificationManager m_notificationMgr;
    private Notification.Builder builder;
    private NotificationManager notificationManager;


    private final long mFrequency = 100;
    private final int TICK_WHAT = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            updateNotification();
            sendMessageDelayed(Message.obtain(this, TICK_WHAT), mFrequency);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return m_binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        m_stopwatch = new Stopwatch();
        m_notificationMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void createNotification() {
        Context context = getApplicationContext();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        final Resources res = context.getResources();
        builder = new Notification.Builder(this);

        builder
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setAutoCancel(false)
                .setContentTitle(res.getString(R.string.notifytitle));

        notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public void updateNotification() {
        // Log.d(TAG, "updating notification");

        builder.setContentText(getFormattedElapsedTime());
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.PRIORITY_MAX | Notification.FLAG_FOREGROUND_SERVICE;
        notificationManager.notify(NOTIFY_ID, notification);
    }

    public void showNotification() {

        updateNotification();
        mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), mFrequency);
    }

    public void hideNotification() {

        m_notificationMgr.cancel(NOTIFY_ID);
        mHandler.removeMessages(TICK_WHAT);
    }

    public void start() {
        m_stopwatch.start();

        showNotification();
    }

    public void pause() {
        m_stopwatch.pause();

        //hideNotification();
    }

    public void reset() {
        m_stopwatch.reset();
    }

    public long getElapsedTime() {
        return m_stopwatch.getElapsedTime();
    }

    public String getFormattedElapsedTime() {
        return formatElapsedTime(getElapsedTime());
    }

    public boolean isStopwatchRunning() {
        return m_stopwatch.isRunning();
    }

    private String formatElapsedTime(long now) {
        long seconds;
        StringBuilder sb = new StringBuilder();
        seconds = now / 1000;
        sb.append((seconds));
        return sb.toString();
    }
}