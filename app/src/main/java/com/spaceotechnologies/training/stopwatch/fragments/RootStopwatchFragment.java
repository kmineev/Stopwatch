package com.spaceotechnologies.training.stopwatch.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.applications.MyApplication;

import static com.spaceotechnologies.training.stopwatch.activitys.MainActivity.STOPWATCH_FRAGMENT_POSITION;
import static com.spaceotechnologies.training.stopwatch.adapters.TextPagerAdapter.STOPWATCH_NUMBER;
import static com.spaceotechnologies.training.stopwatch.activitys.MainActivity.ANDROID_SWITCHER_FRAGMENT;

/**
 * Created by Kostez on 22.08.2016.
 */
public class RootStopwatchFragment extends Fragment implements ParentRootFragment {

    public static final int STOPWATCH_TIME_LIST_FRAGMENT_POSITION = 1;
    private boolean isStopwatchTimeListVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_root_stopwatch, container, false);

        getActivity().getFragmentManager()
                .beginTransaction()
                .replace(R.id.root_frame_stopwatch,
                        CounterFragment.newInstance(STOPWATCH_NUMBER, MyApplication.getAppContext().getResources().getStringArray(R.array.titles_tabs)[STOPWATCH_NUMBER]),
                        ANDROID_SWITCHER_FRAGMENT + STOPWATCH_NUMBER + ':' + PAGE_ELEMENT)
                .commit();
        return view;
    }

    public void switchFragments() {
        if (!isStopwatchTimeListVisible) {
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.animator.slide_enter_down,
                            R.animator.slide_exit_up,
                            R.animator.slide_enter_down,
                            R.animator.slide_exit_up)
                    .replace(R.id.root_frame_stopwatch,
                            new TimeListFragment(STOPWATCH_NUMBER),
                            ANDROID_SWITCHER_FRAGMENT + STOPWATCH_NUMBER + ':' + STOPWATCH_TIME_LIST_FRAGMENT_POSITION)
                    .commit();
            isStopwatchTimeListVisible = true;
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.animator.slide_enter_down,
                            R.animator.slide_exit_up,
                            R.animator.slide_enter_down,
                            R.animator.slide_exit_up)
                    .replace(R.id.root_frame_stopwatch,
                            CounterFragment.newInstance(STOPWATCH_NUMBER, MyApplication.getAppContext().getResources().getStringArray(R.array.titles_tabs)[STOPWATCH_NUMBER]),
                            ANDROID_SWITCHER_FRAGMENT + STOPWATCH_NUMBER + ':' + STOPWATCH_FRAGMENT_POSITION)
                    .commit();
            isStopwatchTimeListVisible = false;
        }
    }
}
