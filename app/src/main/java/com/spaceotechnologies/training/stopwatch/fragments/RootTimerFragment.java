package com.spaceotechnologies.training.stopwatch.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.applications.MyApplication;

import static com.spaceotechnologies.training.stopwatch.activitys.MainActivity.ANDROID_SWITCHER_FRAGMENT;
import static com.spaceotechnologies.training.stopwatch.adapters.TextPagerAdapter.TIMER_FRAGMENT_POSITION;
import static com.spaceotechnologies.training.stopwatch.adapters.TextPagerAdapter.TIMER_NUMBER;

/**
 * Created by Kostez on 22.08.2016.
 */
public class RootTimerFragment extends Fragment implements ParentRootFragment {

    public static final int TIMER_TIME_LIST_FRAGMENT_POSITION = 1;
    private boolean isTimerTimeListVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_root_timer, container, false);

        getActivity().getFragmentManager()
                .beginTransaction()
                .replace(R.id.root_frame_timer,
                        TimerFragment.newInstance(TIMER_NUMBER, MyApplication.getAppContext().getResources().getStringArray(R.array.titles_tabs)[TIMER_NUMBER]),
                        ANDROID_SWITCHER_FRAGMENT + TIMER_NUMBER + ':' + PAGE_ELEMENT)
                .commit();
        return view;
    }

    public void switchFragments() {
        if (!isTimerTimeListVisible) {
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.animator.slide_enter_down,
                            R.animator.slide_exit_up,
                            R.animator.slide_enter_down,
                            R.animator.slide_exit_up)
                    .replace(R.id.root_frame_timer,
                            new TimeListFragment(TIMER_NUMBER),
                            ANDROID_SWITCHER_FRAGMENT + TIMER_NUMBER + ':' + TIMER_TIME_LIST_FRAGMENT_POSITION)
                    .commit();
            isTimerTimeListVisible = true;
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.animator.slide_enter_down,
                            R.animator.slide_exit_up,
                            R.animator.slide_enter_down,
                            R.animator.slide_exit_up)
                    .replace(R.id.root_frame_timer,
                            TimerFragment.newInstance(TIMER_NUMBER, MyApplication.getAppContext().getResources().getStringArray(R.array.titles_tabs)[TIMER_NUMBER]),
                            ANDROID_SWITCHER_FRAGMENT + TIMER_NUMBER + ':' + TIMER_FRAGMENT_POSITION)
                    .commit();
            isTimerTimeListVisible = false;
        }
    }
}
