package com.spaceotechnologies.training.stopwatch;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    boolean isStoped = false;

    int seconds = 0;
    String colorName = "";
    int backgroundColorOld = R.color.White;
    int backgroundColorNew = -1;
    AutoResizeTextView counter;

    private NotificationManager nm;
    private final int NOTIFICATION_ID = 127;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        runStopwatch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.reset) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void runStopwatch(){
        counter = (AutoResizeTextView) findViewById(R.id.counter);
        final Handler handler = new Handler();

        final Notification.Builder builder = new Notification.Builder(getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_launcher))

                .setAutoCancel(false)
                .setContentTitle("STOPWATCH COUNT:");

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.PRIORITY_MAX |Notification.FLAG_FOREGROUND_SERVICE;
        nm.notify(NOTIFICATION_ID, notification);

        handler.post(new Runnable() {

            @Override
            public void run() {

                String time = String.format("%d", seconds);

                if (!isStoped) {
                    counter.setText(time);
                    seconds++;
                }

                builder.setContentText(time);
                Notification notification = builder.build();
                nm.notify(NOTIFICATION_ID, notification);

                handler.postDelayed(this, 1000);
            }
        });
    }

    public void onClickReset(MenuItem item) {
        seconds = 0;
    }

    public void onClickSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);
        outState.putInt("backgroundColorOld", backgroundColorOld);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        seconds = savedInstanceState.getInt("seconds");
        backgroundColorOld = savedInstanceState.getInt("backgroundColorOld");
    }

    @Override
    protected void onResume() {
        super.onResume();
        colorName = getIntent().getStringExtra("color");
        if (colorName != null) {
            switch (colorName) {
                case "White":
                    backgroundColorNew = R.color.White;
                    break;
                case "Red":
                    backgroundColorNew = R.color.Red;
                    break;
                case "Orange":
                    backgroundColorNew = R.color.Orange;
                    break;
                case "Yellow":
                    backgroundColorNew = R.color.Yellow;
                    break;
                case "Green":
                    backgroundColorNew = R.color.Green;
                    break;
                case "Blue":
                    backgroundColorNew = R.color.Blue;
                    break;
                case "DarkBlue":
                    backgroundColorNew = R.color.DarkBlue;
                    break;
                case "Violet":
                    backgroundColorNew = R.color.Violet;
                    break;
            }
        }

        if (backgroundColorNew != -1) {
            final RelativeLayout counterlL = (RelativeLayout) findViewById(R.id.counterBackground);

            handler = new Handler();

            new Thread(){
                @Override
                public void run(){
                    handler.post(new Runnable(){
                        public void run(){
                            animateBetweenColors(counterlL,
                                    getResources().getColor(backgroundColorOld),
                                    getResources().getColor(backgroundColorNew),
                                    7000);
                            backgroundColorOld = backgroundColorNew;
                        }
                    });
                }
            }.start();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public static void animateBetweenColors(final View viewToAnimateItBackground, final int colorFrom, final int colorTo,
                                            final int durationInMs) {
        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            ColorDrawable colorDrawable = new ColorDrawable(colorFrom);

            @Override
            public void onAnimationUpdate(final ValueAnimator animator) {
                colorDrawable.setColor((Integer) animator.getAnimatedValue());
                viewToAnimateItBackground.setBackgroundDrawable(colorDrawable);
            }
        });
        if (durationInMs >= 0)
            colorAnimation.setDuration(durationInMs);
        colorAnimation.start();
    }

    public void onClickPause(MenuItem item) {
        isStoped = !isStoped;
    }
}