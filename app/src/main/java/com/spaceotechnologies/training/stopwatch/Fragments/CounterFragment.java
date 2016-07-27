package com.spaceotechnologies.training.stopwatch.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.Views.AutoResizeTextView;

/**
 * Created by Kostez on 13.07.2016.
 */
public class CounterFragment extends Fragment {

    private String title;
    private int page;

    private static String tag = "CounterFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(tag, "In the onCreateView() event");
        final View rootView = inflater.inflate(R.layout.fragment_counter, container, false);
        return rootView;
    }

    public static CounterFragment newInstance(int page, String title) {

        Log.d(tag, "In the newInstance() event");
        CounterFragment counterFragment = new CounterFragment();
        Bundle args = new Bundle();
        args.putInt("Page", page);
        args.putString("Title", title);
        counterFragment.setArguments(args);

        return counterFragment;
    }

    public void setText(String text) {
        ((AutoResizeTextView) getView().findViewById(R.id.counter_fr_counter)).setText(text);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(tag, "In the onCreate() event");
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("Page");
        title = getArguments().getString("Title");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(tag, "In the onViewCreated() event");
        super.onViewCreated(view, savedInstanceState);
    }

}