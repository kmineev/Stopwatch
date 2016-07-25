package com.spaceotechnologies.training.stopwatch.Adapters;

/**
 * Created by Kostez on 19.07.2016.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.spaceotechnologies.training.stopwatch.Fragments.CounterFragment;
import com.spaceotechnologies.training.stopwatch.Fragments.TimerFragment;
import com.spaceotechnologies.training.stopwatch.R;

import java.util.ArrayList;

public class TextPagerAdapter extends FragmentPagerAdapter {

    private final int COUNT = 2;
    public ArrayList<Fragment> fragments = new ArrayList<>();
    String tag = "Adapter";
    private Context context;

    private String tabTitles[];

    public TextPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        this.tabTitles = context.getResources().getStringArray(R.array.titles_tabs);
    }

    @Override
    public Fragment getItem(int i) {
        Log.d(tag, "In the getItem() event");
        switch (i) {
            case 0:
                fragments.add(0, CounterFragment.newInstance(0, tabTitles[0]));
                return fragments.get(0);
            case 1:
                fragments.add(1, TimerFragment.newInstance(1, tabTitles[1]));
                return fragments.get(1);
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