package com.spaceotechnologies.training.stopwatch.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.spaceotechnologies.training.stopwatch.data.DatabaseHelperFactory;
import com.spaceotechnologies.training.stopwatch.data.StopwatchTimeTable;
import com.spaceotechnologies.training.stopwatch.data.TimerTimeTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Kostez on 18.08.2016.
 */
public class TimeListFragment extends ListFragment {

    private int number;
    private ArrayList<String> timeArray;

    public TimeListFragment(int number) {
        this.number = number;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timeArray = new ArrayList<>();

        try {
            setTimeArray();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                timeArray);
        setListAdapter(adapter);
    }

    private void setTimeArray() throws SQLException {
        switch (number) {
            case 0:
                Dao<StopwatchTimeTable, Integer> stopwatchTimeTableDao = DatabaseHelperFactory.getDatabaseHelper().getStopwatchTimeTableDao();
                QueryBuilder<StopwatchTimeTable, Integer> queryBuilderStopwatch = stopwatchTimeTableDao.queryBuilder();
                PreparedQuery<StopwatchTimeTable> preparedQuery = queryBuilderStopwatch.prepare();
                Iterator<StopwatchTimeTable> stopwatchTimeTableIterator = stopwatchTimeTableDao.query(preparedQuery).iterator();

                while (stopwatchTimeTableIterator.hasNext()) {
                    timeArray.add(stopwatchTimeTableIterator.next().getStopwatchTimeValue());
                }

                break;
            case 1:
                Dao<TimerTimeTable, Integer> timerTimeTableDao = DatabaseHelperFactory.getDatabaseHelper().getTimerTimeTableDao();
                QueryBuilder<TimerTimeTable, Integer> queryBuilder = timerTimeTableDao.queryBuilder();
                PreparedQuery<TimerTimeTable> preparedQueryTimer = queryBuilder.prepare();
                Iterator<TimerTimeTable> timerTimeTableIterator = timerTimeTableDao.query(preparedQueryTimer).iterator();

                while (timerTimeTableIterator.hasNext()) {
                    timeArray.add(timerTimeTableIterator.next().getTimerTimeValue());
                }

                break;
        }
    }
}