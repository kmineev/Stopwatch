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
    private CounterFragment counterFragment;

    private String color = "";
    private int newBackgroundColor = -1;
    private int oldBackgroundColorARGB = -1;

    private TextPagerAdapter adapter;
    private final long mFrequency = 100;
    private final int TICK_WHAT = 2;
    private final int TICK_TIMER = 5;
    private TimerService m_timerService;
    private TimerFragment timerFragment;

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

        mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), mFrequency);

        timerHandler.sendMessageDelayed(Message.obtain(timerHandler, TICK_TIMER), mFrequency);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new TextPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(adapter);
    }

    // Connection to the backgorund StopwatchService
    private StopwatchService m_stopwatchService;

    private ServiceConnection m_stopwatchServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            m_stopwatchService = ((StopwatchService.LocalBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            m_stopwatchService = null;
        }
    };

    private void bindStopwatchService() {
        bindService(new Intent(this, StopwatchService.class),
                m_stopwatchServiceConn, Context.BIND_AUTO_CREATE);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            updateElapsedTime();
            sendMessageDelayed(Message.obtain(this, TICK_WHAT), mFrequency);
        }
    };

    public void updateElapsedTime() {
        if ( m_stopwatchService != null && counterFragment != null) {
            counterFragment.setText(m_stopwatchService.getFormattedElapsedTime());
        }
    }

    private Handler timerHandler = new Handler() {
        public void handleMessage(Message m) {
            updateLeftTime();
            sendMessageDelayed(Message.obtain(this, TICK_TIMER), mFrequency);
        }
    };

    private void bindTimerService() {
        bindService(new Intent(this, TimerService.class),
                m_timerServiceConn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection m_timerServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            m_timerService = ((TimerService.LocalBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            m_timerService = null;
        }
    };

    public void updateLeftTime() {
        if ( m_timerService != null && timerFragment != null) {
            timerFragment.setText(m_timerService.getFormattedLeftTime());
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(TICK_WHAT);
        unbindStopwatchService();
        super.onDestroy();
    }

    private void unbindStopwatchService() {
        if ( m_stopwatchService != null ) {
            unbindService(m_stopwatchServiceConn);
        }
    }

    public void onClickStartPause(MenuItem item) {
        if  (m_stopwatchService.isStopwatchRunning()) {
            m_stopwatchService.pause();
        } else {
            m_timerService.pause();
            m_stopwatchService.start();
        }
    }

    public void onClickReset(MenuItem item) {
        m_stopwatchService.reset();
    }

    public void onClickStartStopTimer(MenuItem item) {
        if (m_timerService.isTimerRunning()) {
            m_timerService.pause();
        } else {
            m_stopwatchService.pause();
            m_timerService.start();
        }
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

        counterFragment = (CounterFragment) adapter.fragments.get(0);
        m_stopwatchService.start();

        if  (adapter.fragments.get(1) != null) {
            timerFragment = (TimerFragment) adapter.fragments.get(1);
        }

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
}