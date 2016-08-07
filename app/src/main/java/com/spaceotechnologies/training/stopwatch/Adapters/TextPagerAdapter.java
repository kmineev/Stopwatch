package com.spaceotechnologies.training.stopwatch.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.spaceotechnologies.training.stopwatch.Fragments.CounterFragment;
import com.spaceotechnologies.training.stopwatch.Fragments.TimerFragment;
import com.spaceotechnologies.training.stopwatch.R;

import static com.spaceotechnologies.training.stopwatch.Activitys.MainActivity.STOPWATCH_NUMBER;
import static com.spaceotechnologies.training.stopwatch.Activitys.MainActivity.TIMER_NUMBER;

/**
 * Created by Kostez on 01.08.2016.
 */
public class TextPagerAdapter extends FragmentPagerAdapter {

    private final int COUNT = 2;

    private String tabTitles[];

    public TextPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.tabTitles = context.getResources().getStringArray(R.array.titles_tabs);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case STOPWATCH_NUMBER:
                return CounterFragment.newInstance(STOPWATCH_NUMBER, tabTitles[STOPWATCH_NUMBER]);
            case TIMER_NUMBER:
                return TimerFragment.newInstance(TIMER_NUMBER, tabTitles[TIMER_NUMBER]);
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
