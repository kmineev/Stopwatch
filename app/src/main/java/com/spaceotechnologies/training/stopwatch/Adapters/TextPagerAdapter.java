package com.spaceotechnologies.training.stopwatch.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.spaceotechnologies.training.stopwatch.Fragments.CounterFragment;
import com.spaceotechnologies.training.stopwatch.Fragments.TimerFragment;
import com.spaceotechnologies.training.stopwatch.R;

/**
 * Created by Kostez on 01.08.2016.
 */
public class TextPagerAdapter extends FragmentPagerAdapter {

    private final int COUNT = 2;
    String tag = "Adapter";

    private String tabTitles[];

    public TextPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.tabTitles = context.getResources().getStringArray(R.array.titles_tabs);
    }

    @Override
    public Fragment getItem(int i) {
        Log.d(tag, "In the getItem() event");
        switch (i) {
            case 0:
                return CounterFragment.newInstance(0, tabTitles[0]);
            case 1:
                return TimerFragment.newInstance(1, tabTitles[1]);
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
