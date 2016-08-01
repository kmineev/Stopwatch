package com.spaceotechnologies.training.stopwatch.Activitys;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.spaceotechnologies.training.stopwatch.Adapters.TextPagerAdapter;
import com.spaceotechnologies.training.stopwatch.Fragments.CounterFragment;
import com.spaceotechnologies.training.stopwatch.Fragments.TimerFragment;
import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.Services.StopwatchService;
import com.spaceotechnologies.training.stopwatch.Services.TimerService;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String color = "";
    private int newBackgroundColor = -1;
    private int oldBackgroundColorARGB = -1;

    private TextPagerAdapter adapter;
    private final long frequency = 100;
    private final int TICK_STOPWATCH = 2;
    private final int TICK_TIMER = 5;
    private TimerService timerService;
    private StopwatchService stopwatchService;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        startService(new Intent(this, StopwatchService.class));
        bindStopwatchService();

        startService(new Intent(this, TimerService.class));
        bindTimerService();

        stopwatchHandler.sendMessageDelayed(Message.obtain(stopwatchHandler, TICK_STOPWATCH), frequency);
        timerHandler.sendMessageDelayed(Message.obtain(timerHandler, TICK_TIMER), frequency);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new TextPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        if (stopwatchService != null) {
                            if (stopwatchService.isStopwatchRunning()) {
                                showPauseButtons();
                            } else {
                                showStartButtons();
                            }
                        }
                        break;
                    case 1:
                        if (timerService != null) {
                            if (timerService.isTimerRunning()) {
                                showPauseButtons();
                            } else {
                                showStartButtons();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    protected Fragment findFragmentByPosition(int position) {
        int pagerId = this.viewPager.getId();
        return getSupportFragmentManager().findFragmentByTag(getFragmentTag(pagerId, position));
    }

    private String getFragmentTag(int viewPagerId, int fragmentPosition) {
        return getResources().getString(R.string.android_switcher) + viewPagerId + ':' + fragmentPosition;
    }

    // Connection to the backgorund StopwatchService

    private ServiceConnection stopwatchServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            stopwatchService = ((StopwatchService.LocalBinder) service).getService();
            stopwatchService.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            stopwatchService = null;
        }
    };

    private void bindStopwatchService() {
        bindService(new Intent(this, StopwatchService.class),
                stopwatchServiceConnection, BIND_AUTO_CREATE);
    }

    private Handler stopwatchHandler = new Handler() {
        public void handleMessage(Message m) {
            updateElapsedTime();
            sendMessageDelayed(Message.obtain(this, TICK_STOPWATCH), frequency);
        }
    };

    public void updateElapsedTime() {
        if (stopwatchService != null && findFragmentByPosition(0) != null) {
            ((CounterFragment) findFragmentByPosition(0)).setText(stopwatchService.getFormattedElapsedTime());
        }
    }

    private Handler timerHandler = new Handler() {
        public void handleMessage(Message m) {
            updateLeftTime();
            sendMessageDelayed(Message.obtain(this, TICK_TIMER), frequency);
        }
    };

    private void bindTimerService() {
        bindService(new Intent(this, TimerService.class),
                timerServiceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection timerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            timerService = ((TimerService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            timerService = null;
        }
    };

    public void updateLeftTime() {
        if (timerService != null && findFragmentByPosition(1) != null) {
            ((TimerFragment) findFragmentByPosition(1)).setText(timerService.getFormattedLeftTime());
            if (timerService.isTimeout() && viewPager.getCurrentItem() == 1) {
                showStartButtons();
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopwatchHandler.removeMessages(TICK_STOPWATCH);
        unbindStopwatchService();
        unbindTimerService();
        super.onDestroy();
    }

    private void unbindStopwatchService() {
        if (stopwatchService != null) {
            unbindService(stopwatchServiceConnection);
        }
    }

    private void unbindTimerService() {
        if (timerService != null) {
            unbindService(timerServiceConnection);
        }
    }


    public void onClickStartPause(MenuItem item) {

        switch (viewPager.getCurrentItem()) {
            case 0:
                if (stopwatchService.isStopwatchRunning()) {
                    showStartButtons();
                    stopwatchService.pause();
                } else {
                    showPauseButtons();
                    timerService.pause();
                    stopwatchService.start();
                }
                break;
            case 1:
                if (timerService.isTimerRunning()) {
                    showStartButtons();
                    timerService.pause();
                } else {
                    if (!timerService.isTimeout()) {
                        showPauseButtons();
                        stopwatchService.pause();
                        timerService.start();
                    }
                }
                break;
        }
    }

    public void onClickReset(MenuItem item) {
        menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_play_arrow_24dp));
        stopwatchService.reset();
    }

    public void onClickSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        color = data.getStringExtra(getResources().getString(R.string.color));
        int colorARGB = getColor(color);

        setBackgroundColor(colorARGB);
    }

    private int getColor(String nameColor) {
        //здесь кейс не получается сделать, выводит constant expression required

        if (nameColor.equals(getResources().getString(R.string.White))) {
            newBackgroundColor = R.color.White;
        } else if (nameColor.equals(getResources().getString(R.string.Red))) {
            newBackgroundColor = R.color.Red;
        } else if (nameColor.equals(getResources().getString(R.string.Orange))) {
            newBackgroundColor = R.color.Orange;
        } else if (nameColor.equals(getResources().getString(R.string.Yellow))) {
            newBackgroundColor = R.color.Yellow;
        } else if (nameColor.equals(getResources().getString(R.string.Green))) {
            newBackgroundColor = R.color.Green;
        } else if (nameColor.equals(getResources().getString(R.string.Blue))) {
            newBackgroundColor = R.color.Blue;
        } else if (nameColor.equals(getResources().getString(R.string.DarkBlue))) {
            newBackgroundColor = R.color.DarkBlue;
        } else if (nameColor.equals(getResources().getString(R.string.Violet))) {
            newBackgroundColor = R.color.Violet;
        } else if (nameColor.equals(getResources().getString(R.string.Black))) {
            newBackgroundColor = R.color.Black;
        }

        newBackgroundColor = getResources().getColor(newBackgroundColor);

        return newBackgroundColor;
    }

    private void setBackgroundColor(final int newColor) {

        final LinearLayout counterlL = (LinearLayout) findViewById(R.id.counterBackground);

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), oldBackgroundColorARGB, newColor);
        colorAnimation.setDuration(5000);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                counterlL.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        oldBackgroundColorARGB = newColor;
        colorAnimation.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    private void showPauseButtons() {
        MenuItem menuItem = menu.getItem(0).setTitle(R.string.action_pause);
        menuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_pause_24dp));
    }

    private void showStartButtons() {
        MenuItem menuItem = menu.getItem(0).setTitle(R.string.action_start);
        menuItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_play_arrow_24dp));
    }


}