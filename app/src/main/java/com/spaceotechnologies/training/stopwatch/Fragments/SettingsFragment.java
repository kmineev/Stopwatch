package com.spaceotechnologies.training.stopwatch.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.spaceotechnologies.training.stopwatch.R;

/**
 * Created by Kostez on 13.07.2016.
 */
public class SettingsFragment extends ListFragment {


    private final int BACKGROUND_COLOR_NUMBER = 0;
    private final int FOREGROUND_COLOR_NUMBER = 1;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListAdapter adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                getActivity().getResources().getTextArray(R.array.string_array_settings));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (position) {
            case BACKGROUND_COLOR_NUMBER:
                ColorsListFragment colorsListFragment = new ColorsListFragment();
                fragmentTransaction.replace(R.id.settings_content, colorsListFragment);
                break;
            case FOREGROUND_COLOR_NUMBER:
                System.out.println(1);
                break;
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}