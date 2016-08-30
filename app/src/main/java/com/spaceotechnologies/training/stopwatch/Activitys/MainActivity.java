package com.spaceotechnologies.training.stopwatch.activitys;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Fragment;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.adapters.TextPagerAdapter;
import com.spaceotechnologies.training.stopwatch.applications.MyApplication;
import com.spaceotechnologies.training.stopwatch.data.ColorTable;
import com.spaceotechnologies.training.stopwatch.data.DatabaseHelperFactory;
import com.spaceotechnologies.training.stopwatch.data.StopwatchTimeTable;
import com.spaceotechnologies.training.stopwatch.data.TimerTimeTable;
import com.spaceotechnologies.training.stopwatch.fragments.CounterFragment;
import com.spaceotechnologies.training.stopwatch.fragments.CuttoffTimeFragment;
import com.spaceotechnologies.training.stopwatch.fragments.RootStopwatchFragment;
import com.spaceotechnologies.training.stopwatch.fragments.RootTimerFragment;
import com.spaceotechnologies.training.stopwatch.fragments.TimerFragment;
import com.spaceotechnologies.training.stopwatch.services.StopwatchService;
import com.spaceotechnologies.training.stopwatch.services.TimerService;

import java.sql.SQLException;
import java.util.Iterator;

import static com.spaceotechnologies.training.stopwatch.adapters.TextPagerAdapter.STOPWATCH_NUMBER;
import static com.spaceotechnologies.training.stopwatch.adapters.TextPagerAdapter.TIMER_NUMBER;
import static com.spaceotechnologies.training.stopwatch.adapters.TextPagerAdapter.STOPWATCH_FRAGMENT_POSITION;
import static com.spaceotechnologies.training.stopwatch.adapters.TextPagerAdapter.TIMER_FRAGMENT_POSITION;
import static com.spaceotechnologies.training.stopwatch.services.BaseService.BROADCAST_ACTION;
import static com.spaceotechnologies.training.stopwatch.services.BaseService.CURRENT_PAGE;
import static com.spaceotechnologies.training.stopwatch.fragments.ColorsListFragment.COLOR_EXTRA;

public class MainActivity extends AppCompatActivity {

    public static final String ANDROID_SWITCHER = "android:switcher:";
    public static final String ANDROID_SWITCHER_FRAGMENT = "android:switcher:fragment:";

    private static final String BACKGROUND_COLOR = "BackgroundColor";

    private static final int REQUEST_CODE = 1;
    private static final long FREQUENCY = 100;
    private static final int TICK_STOPWATCH = 2;
    private static final int TICK_TIMER = 5;
    private static final int ANIMATION_DURATION = 5000;

    private String color;
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
    private CuttoffTimeFragment cuttoffTimeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                currentPage = intent.getIntExtra(CURRENT_PAGE, STOPWATCH_NUMBER);
                setupViewPager(viewPager);
            }
        };

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        stopwatchHandler.sendMessageDelayed(Message.obtain(stopwatchHandler, TICK_STOPWATCH), FREQUENCY);
        timerHandler.sendMessageDelayed(Message.obtain(timerHandler, TICK_TIMER), FREQUENCY);

        cuttoffTimeFragment = (CuttoffTimeFragment) getFragmentManager().findFragmentById(R.id.cutoff_time_fragment);

        try {
            final Dao<ColorTable, Integer> colorTableDao = DatabaseHelperFactory.getDatabaseHelper().getColorTableDao();
            final QueryBuilder<ColorTable, Integer> queryBuilder = colorTableDao.queryBuilder();
            queryBuilder.where().eq(ColorTable.NAME_FIELD, BACKGROUND_COLOR);
            final PreparedQuery<ColorTable> preparedQuery = queryBuilder.prepare();
            final Iterator<ColorTable> colorTableIterator = colorTableDao.query(preparedQuery).iterator();

            if (colorTableIterator.hasNext()) {
                while (colorTableIterator.hasNext()) {
                    final ColorTable cTable = colorTableIterator.next();
                    if (cTable.getColorName().equals(BACKGROUND_COLOR)) {
                        color = cTable.getColorValue();
                    }
                }
            } else {
                color = getResources().getString(R.string.White);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final LinearLayout counterlL = (LinearLayout) findViewById(R.id.counterBackground);
        counterlL.setBackgroundColor(getColor(color));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new TextPagerAdapter(getFragmentManager(), MainActivity.this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPage);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

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
                cuttoffTimeFragment.showCorrectButtons();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    protected Fragment findFragmentByPosition(int page, int position) {
        return getFragmentManager().findFragmentByTag(getFragmentTag(page, position));
    }

    private String getFragmentTag(int page, int fragmentPosition) {
        return ANDROID_SWITCHER_FRAGMENT + page + ':' + fragmentPosition;
    }

    protected Fragment findFragmentByViewByPosition(int id, int position) {
        return getFragmentManager().findFragmentByTag(getFragmentTagByView(id, position));
    }

    private String getFragmentTagByView(int viewId, int position) {
        return ANDROID_SWITCHER + viewId + ':' + position;
    }

    private ServiceConnection stopwatchServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            stopwatchService = ((StopwatchService.LocalBinder) service).getService();
            cuttoffTimeFragment.setStopwatchService(stopwatchService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private void bindStopwatchService() {
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
        if (stopwatchService != null && findFragmentByPosition(STOPWATCH_NUMBER, STOPWATCH_FRAGMENT_POSITION) != null) {
            ((CounterFragment) findFragmentByPosition(STOPWATCH_NUMBER, STOPWATCH_FRAGMENT_POSITION)).setText(stopwatchService.getFormattedElapsedTime());
        }
    }

    private Handler timerHandler = new Handler() {
        public void handleMessage(Message m) {
            updateLeftTime();
            sendMessageDelayed(Message.obtain(this, TICK_TIMER), FREQUENCY);
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
            cuttoffTimeFragment.setTimerService(timerService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //timerService = null;
        }
    };

    public void updateLeftTime() {
        if (timerService != null && findFragmentByPosition(TIMER_NUMBER, TIMER_FRAGMENT_POSITION) != null) {
            ((TimerFragment) findFragmentByPosition(TIMER_NUMBER, TIMER_FRAGMENT_POSITION)).setText(timerService.getFormattedLeftTime());
            if (timerService.isTimeout() && viewPager.getCurrentItem() == TIMER_NUMBER) {
                showStartButtons();
            }
        }
    }

    @Override
    protected void onDestroy() {

        try {
            final Dao<ColorTable, Integer> colorTableDao = DatabaseHelperFactory.getDatabaseHelper().getColorTableDao();
            final QueryBuilder<ColorTable, Integer> queryBuilder = colorTableDao.queryBuilder();
            queryBuilder.where().eq(ColorTable.NAME_FIELD, BACKGROUND_COLOR);
            final PreparedQuery<ColorTable> preparedQuery = queryBuilder.prepare();
            final Iterator<ColorTable> colorTableIterator = colorTableDao.query(preparedQuery).iterator();

            if (colorTableIterator.hasNext()) {
                while (colorTableIterator.hasNext()) {
                    final ColorTable cTable = colorTableIterator.next();
                    if (cTable.getColorName().equals(BACKGROUND_COLOR)) {
                        cTable.setColorValue(color);
                        colorTableDao.update(cTable);
                    }
                }
            } else {
                final ColorTable colorTable = new ColorTable();
                colorTable.setColorName(BACKGROUND_COLOR);
                colorTable.setColorValue(color);
                colorTableDao.create(colorTable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stopwatchHandler.removeMessages(TICK_STOPWATCH);
        unbindStopwatchService();
        unbindTimerService();
        unregisterReceiver(broadcastReceiver);
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

        if (data == null) {
            return;
        }

        color = data.getStringExtra(COLOR_EXTRA);
        int colorARGB = getColor(color);

        setBackgroundColor(colorARGB);
    }

    private int getColor(String nameColor) {

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

    public void onClickSaveTime(MenuItem item) throws SQLException {
        switch (viewPager.getCurrentItem()) {
            case STOPWATCH_NUMBER:
                Dao<StopwatchTimeTable, Integer> stopwatchTimeTableDao = DatabaseHelperFactory.getDatabaseHelper().getStopwatchTimeTableDao();
                StopwatchTimeTable stopwatchTimeTable = new StopwatchTimeTable();
                stopwatchTimeTable.setStopwatchTimeValue(stopwatchService.getFormattedElapsedTime());
                stopwatchTimeTableDao.create(stopwatchTimeTable);
                break;
            case TIMER_NUMBER:
                Dao<TimerTimeTable, Integer> timerTimeTableDao = DatabaseHelperFactory.getDatabaseHelper().getTimerTimeTableDao();
                TimerTimeTable timerTimeTable = new TimerTimeTable();
                timerTimeTable.setTimerTimeValue(timerService.getFormattedLeftTime());
                timerTimeTableDao.create(timerTimeTable);
                break;
        }
    }

    public void onClickShowSavedTime(MenuItem item) throws SQLException {

        switch (viewPager.getCurrentItem()) {
            case STOPWATCH_NUMBER:
                ((RootStopwatchFragment) findFragmentByViewByPosition(viewPager.getId(), STOPWATCH_NUMBER)).switchFragments();
                break;

            case TIMER_NUMBER:
                ((RootTimerFragment) findFragmentByViewByPosition(viewPager.getId(), TIMER_NUMBER)).switchFragments();
                break;
        }
    }
}