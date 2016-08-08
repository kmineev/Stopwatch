package com.spaceotechnologies.training.stopwatch.activitys;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.spaceotechnologies.training.stopwatch.adapters.TextPagerAdapter;
import com.spaceotechnologies.training.stopwatch.applications.MyApplication;
import com.spaceotechnologies.training.stopwatch.fragments.CounterFragment;
import com.spaceotechnologies.training.stopwatch.fragments.TimerFragment;
import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.services.StopwatchService;
import com.spaceotechnologies.training.stopwatch.services.TimerService;

public class MainActivity extends AppCompatActivity {

    public static final int STOPWATCH_NUMBER = 0;
    public static final int TIMER_NUMBER = 1;

    private static final int REQUEST_CODE = 1;
    private static final long FREQUENCY = 100;
    private static final int TICK_STOPWATCH = 2;
    private static final int TICK_TIMER = 5;
    private static final int ANIMATION_DURATION = 5000;

    private String color = "";
    private int newBackgroundColor = -1;
    private int oldBackgroundColorARGB = -1;
    private int currentPage = 0;

    private Menu menu;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextPagerAdapter adapter;
    private TimerService timerService;
    private StopwatchService stopwatchService;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getResources().getString(R.string.log_app), "MainActivity onCreate");
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startService(new Intent(this, StopwatchService.class));
        bindStopwatchService();

        startService(new Intent(this, TimerService.class));
        bindTimerService();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                currentPage = intent.getIntExtra(getResources().getString(R.string.current_page), STOPWATCH_NUMBER);
                setupViewPager(viewPager);
            }
        };

        IntentFilter intentFilter = new IntentFilter(getResources().getString(R.string.broadcast_action));
        registerReceiver(broadcastReceiver, intentFilter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        stopwatchHandler.sendMessageDelayed(Message.obtain(stopwatchHandler, TICK_STOPWATCH), FREQUENCY);
        timerHandler.sendMessageDelayed(Message.obtain(timerHandler, TICK_TIMER), FREQUENCY);
    }

    @Override
    protected void onStart() {
        Log.d(getResources().getString(R.string.log_app), "MainActivity onStart");
        super.onStart();
    }

    private void setupViewPager(ViewPager viewPager) {
        Log.d(getResources().getString(R.string.log_app), "MainActivity setupViewPager");
        adapter = new TextPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPage);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case STOPWATCH_NUMBER:
                        if (stopwatchService != null) {
                            if (stopwatchService.isStopwatchRunning()) {
                                showPauseButtons();
                            } else {
                                showStartButtons();
                            }
                        }
                        break;
                    case TIMER_NUMBER:
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

    private ServiceConnection stopwatchServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(getResources().getString(R.string.log_app), "MainActivity onServiceConnected Stopwatch");
            stopwatchService = ((StopwatchService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(getResources().getString(R.string.log_app), "MainActivity onServiceDisconnected Stopwatch");
        }
    };

    private void bindStopwatchService() {
        Log.d(getResources().getString(R.string.log_app), "MainActivity bindStopwatchService");

        bindService(new Intent(this, StopwatchService.class),
                stopwatchServiceConnection, BIND_AUTO_CREATE);
    }

    private Handler stopwatchHandler = new Handler() {
        public void handleMessage(Message m) {
            updateElapsedTime();
            sendMessageDelayed(Message.obtain(this, TICK_STOPWATCH), FREQUENCY);
        }
    };

    public void updateElapsedTime() {
        if (stopwatchService != null && findFragmentByPosition(STOPWATCH_NUMBER) != null) {
            ((CounterFragment) findFragmentByPosition(STOPWATCH_NUMBER)).setText(stopwatchService.getFormattedElapsedTime());
        }
    }

    private Handler timerHandler = new Handler() {
        public void handleMessage(Message m) {
            updateLeftTime();
            sendMessageDelayed(Message.obtain(this, TICK_TIMER), FREQUENCY);
        }
    };

    private void bindTimerService() {
        Log.d(getResources().getString(R.string.log_app), "MainActivity bindTimerService");
        bindService(new Intent(this, TimerService.class),
                timerServiceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection timerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(getResources().getString(R.string.log_app), "MainActivity onServiceConnected Timer");
            timerService = ((TimerService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(getResources().getString(R.string.log_app), "MainActivity onServiceDisconnected Timer");
            //timerService = null;
        }
    };

    public void updateLeftTime() {
        if (timerService != null && findFragmentByPosition(TIMER_NUMBER) != null) {
            ((TimerFragment) findFragmentByPosition(TIMER_NUMBER)).setText(timerService.getFormattedLeftTime());
            if (timerService.isTimeout() && viewPager.getCurrentItem() == TIMER_NUMBER) {
                showStartButtons();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(getResources().getString(R.string.log_app), "MainActivity onDestroy");
        stopwatchHandler.removeMessages(TICK_STOPWATCH);
        unbindStopwatchService();
        unbindTimerService();
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private void unbindStopwatchService() {
        Log.d(getResources().getString(R.string.log_app), "MainActivity unbindStopwatchService");
        if (stopwatchService != null) {
            unbindService(stopwatchServiceConnection);
        }
    }

    private void unbindTimerService() {
        Log.d(getResources().getString(R.string.log_app), "MainActivity unbindTimerService");
        if (timerService != null) {
            unbindService(timerServiceConnection);
        }
    }

    public void onClickStartPause(MenuItem item) {

        switch (viewPager.getCurrentItem()) {
            case STOPWATCH_NUMBER:
                if (stopwatchService.isStopwatchRunning()) {
                    showStartButtons();
                    stopwatchService.pause();
                } else {
                    showPauseButtons();
                    timerService.pause();
                    stopwatchService.start();
                }
                break;
            case TIMER_NUMBER:
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
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(getResources().getString(R.string.log_app), "MainActivity onActivityResult");

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

        newBackgroundColor = ContextCompat.getColor(MyApplication.getAppContext(), newBackgroundColor);

        return newBackgroundColor;
    }

    private void setBackgroundColor(final int newColor) {

        final LinearLayout counterlL = (LinearLayout) findViewById(R.id.counterBackground);

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), oldBackgroundColorARGB, newColor);
        colorAnimation.setDuration(ANIMATION_DURATION);
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