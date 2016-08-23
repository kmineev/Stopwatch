package com.spaceotechnologies.training.stopwatch.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.fragments.RootStopwatchFragment;
import com.spaceotechnologies.training.stopwatch.fragments.RootTimerFragment;

import static com.spaceotechnologies.training.stopwatch.activitys.MainActivity.STOPWATCH_NUMBER;
import static com.spaceotechnologies.training.stopwatch.activitys.MainActivity.TIMER_NUMBER;

/**
 * Created by Kostez on 01.08.2016.
 */
public class TextPagerAdapter extends android.support.v13.app.FragmentPagerAdapter {

    private final int COUNT = 2;

    private String tabTitles[];
    private Context context;

    public TextPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.tabTitles = context.getResources().getStringArray(R.array.titles_tabs);
    }

//    CounterFragment.newInstance(STOPWATCH_NUMBER, tabTitles[STOPWATCH_NUMBER]);
//    TimerFragment.newInstance(TIMER_NUMBER, tabTitles[TIMER_NUMBER]);
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case STOPWATCH_NUMBER:
                return new RootStopwatchFragment();
            case TIMER_NUMBER:
                return new RootTimerFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
