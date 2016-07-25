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
import android.util.Log;

import com.spaceotechnologies.training.stopwatch.Activitys.MainActivity;
import com.spaceotechnologies.training.stopwatch.Base.Timer;
import com.spaceotechnologies.training.stopwatch.R;

public class TimerService extends Service {

    private static final int NOTIFY_ID = 201;
    private Timer m_timer;
    private NotificationManager m_notificationMgr;
    private Notification.Builder builder;
    private NotificationManager notificationManager;
    private final int startValueTimer = 10000;
    private final long mFrequency = 100;
    private final int TICK_WHAT = 5;
    private LocalBinder m_binder = new LocalBinder();
    private boolean isFinalNotification = false;
    private Resources res;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return m_binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        m_timer = new Timer();
        m_notificationMgr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        createNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
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
            sendMessageDelayed(Message.obtain(this, TICK_WHAT), mFrequency);
        }
    };


    public void createNotification() {
        Context context = getApplicationContext();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        res = context.getResources();
        builder = new Notification.Builder(this);

        builder
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setAutoCancel(false)
                .setContentTitle(res.getString(R.string.timernotifytitle));

        notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public void updateNotification() {
        builder.setContentText(getFormattedLeftTime());
        Notification notification;

        if (getDifference() < 1000) {
            if (!isFinalNotification) {
                isFinalNotification = true;
                builder.setTicker(res.getString(R.string.timeout)).setWhen(System.currentTimeMillis());
                notification = builder.build();
                notification.flags |= Notification.FLAG_NO_CLEAR | Notification.PRIORITY_MAX |Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_INSISTENT;;
                notification.defaults = Notification.DEFAULT_SOUND |
                        Notification.DEFAULT_VIBRATE;
                notificationManager.notify(NOTIFY_ID, notification);
                pause();
            }
        } else {
            notification = builder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR | Notification.PRIORITY_MAX |Notification.FLAG_FOREGROUND_SERVICE;
            notificationManager.notify(NOTIFY_ID, notification);
        }
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
        if (!isFinalNotification) {
            m_timer.start();

            showNotification();
        }
    }

    public void pause() {
        m_timer.pause();

        //hideNotification();
    }

    public void reset() {
        m_timer.reset();
    }

    public long getElapsedTime() {
        return m_timer.getElapsedTime();
    }

    public String getFormattedLeftTime() {
        return formatLeftTime(getDifference());
    }

    private long getDifference() {
        return startValueTimer - getElapsedTime();
    }

    public boolean isTimerRunning() {
        return m_timer.isRunning();
    }

    private String formatLeftTime(long now) {
        long seconds;
        StringBuilder sb = new StringBuilder();
        seconds = now / 1000;
        sb.append((seconds));
        return sb.toString();
    }
}
