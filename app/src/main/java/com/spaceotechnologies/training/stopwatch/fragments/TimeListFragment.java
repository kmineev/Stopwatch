package com.spaceotechnologies.training.stopwatch.fragments;

import android.app.ListFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.applications.MyApplication;
import com.spaceotechnologies.training.stopwatch.data.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by Kostez on 18.08.2016.
 */
public class TimeListFragment extends ListFragment {

    private String tableName = "";

    public TimeListFragment(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper mDatabaseHelper;
        SQLiteDatabase mSqLiteDatabase;

        mDatabaseHelper = new DatabaseHelper(MyApplication.getAppContext());
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        Cursor cursor = mSqLiteDatabase.query(tableName, new String[]{BaseColumns._ID, MyApplication.getAppContext().getResources().getString(R.string.time_name_column)},
                null, null,
                null, null, null);

        ArrayList<String> timeArray = new ArrayList<>();

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            String time = cursor.getString(cursor.getColumnIndex(MyApplication.getAppContext().getResources().getString(R.string.time_name_column)));
            timeArray.add(time);
        }

        cursor.close();

        ListAdapter adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                timeArray);
        setListAdapter(adapter);
    }
}