package com.spaceotechnologies.training.stopwatch.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.applications.MyApplication;

import static com.spaceotechnologies.training.stopwatch.activitys.MainActivity.TIMER_NUMBER;

/**
 * Created by Kostez on 22.08.2016.
 */
public class RootTimerFragment extends Fragment{

    private static final int PAGE_ELEMENT = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_root_timer, container, false);

        getActivity().getFragmentManager()
                .beginTransaction()
                .replace(R.id.root_frame_timer,
                        TimerFragment.newInstance(TIMER_NUMBER, MyApplication.getAppContext().getResources().getStringArray(R.array.titles_tabs)[TIMER_NUMBER]),
                        getResources().getString(R.string.android_switcher_fragment) + TIMER_NUMBER + ':' + PAGE_ELEMENT)
                .commit();
        return view;
    }
}
