package com.spaceotechnologies.training.stopwatch.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.spaceotechnologies.training.stopwatch.R;

/**
 * Created by Kostez on 13.07.2016.
 */
public class ColorsListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListAdapter adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                getActivity().getResources().getTextArray(R.array.string_array_colors));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String colorName = (String) l.getAdapter().getItem(position);
        Intent intent = new Intent();
        intent.putExtra(getResources().getString(R.string.color), colorName);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}