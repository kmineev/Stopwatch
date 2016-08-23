package com.spaceotechnologies.training.stopwatch.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.applications.MyApplication;
import com.spaceotechnologies.training.stopwatch.views.AutoResizeTextView;

/**
 * Created by Kostez on 19.07.2016.
 */
public class TimerFragment extends Fragment {

    private String title;
    private int page;
    private AutoResizeTextView autoResizeTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_timer, container, false);
        return rootView;
    }

    public static TimerFragment newInstance(int page, String title) {
        TimerFragment timerFragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putInt(MyApplication.getAppContext().getString(R.string.page), page);
        args.putString(MyApplication.getAppContext().getString(R.string.title), title);
        timerFragment.setArguments(args);
        return timerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        page = getArguments().getInt(MyApplication.getAppContext().getString(R.string.page));
//        title = getArguments().getString(MyApplication.getAppContext().getString(R.string.title));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        autoResizeTextView = ((AutoResizeTextView) getView().findViewById(R.id.timer_fr_timer));
        super.onViewCreated(view, savedInstanceState);
    }

    public void setText(String text) {
        autoResizeTextView.setText(text);
    }
}