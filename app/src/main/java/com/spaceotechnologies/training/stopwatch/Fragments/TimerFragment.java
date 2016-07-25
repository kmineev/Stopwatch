package com.spaceotechnologies.training.stopwatch.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.Views.AutoResizeTextView;

/**
 * Created by Kostez on 19.07.2016.
 */
public class TimerFragment extends Fragment {

    private String title;
    private int page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_timer, container, false);
        return rootView;
    }

    public static TimerFragment newInstance(int page, String title) {
        TimerFragment timerFragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putInt("Page", page);
        args.putString("Title", title);
        timerFragment.setArguments(args);
        return timerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("Page");
        title = getArguments().getString("Title");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        this.runStopwatch();
    }

    public void setText(String text) {
        ((AutoResizeTextView) getView().findViewById(R.id.timer_fr_timer)).setText(text);
    }
}
