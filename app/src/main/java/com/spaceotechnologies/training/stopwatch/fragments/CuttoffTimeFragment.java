package com.spaceotechnologies.training.stopwatch.fragments;


import android.app.Fragment;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.adapters.RecyclerViewAdapter;
import com.spaceotechnologies.training.stopwatch.applications.MyApplication;
import com.spaceotechnologies.training.stopwatch.data.DatabaseHelper;
import com.spaceotechnologies.training.stopwatch.services.StopwatchService;
import com.spaceotechnologies.training.stopwatch.services.TimerService;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInRightAnimator;

import static com.spaceotechnologies.training.stopwatch.activitys.MainActivity.STOPWATCH_NUMBER;
import static com.spaceotechnologies.training.stopwatch.activitys.MainActivity.TIMER_NUMBER;

/**
 * Created by Kostez on 17.08.2016.
 */
public class CuttoffTimeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapterStopwatch;
    private RecyclerViewAdapter recyclerViewAdapterTimer;
    private ArrayList<String> listItemsStopwatch = new ArrayList<String>();
    private ArrayList<String> listItemsTimer = new ArrayList<String>();
    private ImageButton cutouffButton;
    private View rootView;
    private ViewPager viewPager;
    private boolean isAddButtonStopwatchClicked = false;
    private boolean isAddButtonTimerClicked = false;
    private StopwatchService stopwatchService;
    private TimerService timerService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cutoff_time, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = new OvershootInRightAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.getItemAnimator().setAddDuration(200);

        recyclerViewAdapterStopwatch = new RecyclerViewAdapter(getActivity(), listItemsStopwatch);
        recyclerViewAdapterTimer = new RecyclerViewAdapter(getActivity(), listItemsTimer);

        recyclerView.setAdapter(new ScaleInAnimationAdapter(recyclerViewAdapterStopwatch));

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cutouffButton = (ImageButton) getActivity().findViewById(R.id.button_cutout);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        cutouffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());

                switch (viewPager.getCurrentItem()) {
                    case STOPWATCH_NUMBER:
                        if (!isAddButtonStopwatchClicked) {
                            isAddButtonStopwatchClicked = true;
                            showCorrectButtons();
                        }
                        addNewItemStopwatch(stopwatchService.getFormattedElapsedTime());
                        if (prefs.getBoolean(getString(R.string.save_cutoff), false)) {
                            putIntoDatabase(stopwatchService.getFormattedElapsedTime(), getResources().getString(R.string.cutoff_stopwatch_table));
                        }
                        break;
                    case TIMER_NUMBER:
                        if (!isAddButtonTimerClicked) {
                            isAddButtonTimerClicked = true;
                            showCorrectButtons();
                        }
                        addNewItemTimer(timerService.getFormattedLeftTime());
                        if (prefs.getBoolean(getString(R.string.save_cutoff), false)) {
                            putIntoDatabase(timerService.getFormattedLeftTime(), getResources().getString(R.string.cutoff_timer_table));
                        }
                        break;
                }
            }
        });
    }

    private void putIntoDatabase(String timeValue, String table){
        ContentValues values = new ContentValues();
        values.put(MyApplication.getAppContext().getResources().getString(R.string.cutoff_value_value), timeValue);
        DatabaseHelper mDatabaseHelper;
        SQLiteDatabase mSqLiteDatabase;
        mDatabaseHelper = new DatabaseHelper(MyApplication.getAppContext());
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        mSqLiteDatabase.insert(table, null, values);
    }

    public void addNewItemStopwatch(String itemText) {
        recyclerViewAdapterStopwatch.add(itemText, RecyclerViewAdapter.LAST_POSITION);
        recyclerView.scrollToPosition(recyclerViewAdapterStopwatch.getItemCount() - 1);
    }

    public void addNewItemTimer(String itemText) {
        recyclerViewAdapterTimer.add(itemText, RecyclerViewAdapter.LAST_POSITION);
        recyclerView.scrollToPosition(recyclerViewAdapterTimer.getItemCount() - 1);
    }

    public void setVisible() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        params.weight = 300;
        rootView.setLayoutParams(params);
    }

    public void setInvisible() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, 0);
        rootView.setLayoutParams(params);
    }

    public void setRecyclerViewAdaperStopwatch() {
        this.recyclerView.setAdapter(recyclerViewAdapterStopwatch);
    }

    public void setRecyclerViewAdaperTimer() {
        this.recyclerView.setAdapter(recyclerViewAdapterTimer);
    }

    private void showAddListButton() {
        cutouffButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.button_create_list, null));
    }

    private void showAddTimeButton() {
        cutouffButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.button_add, null));
    }

    public void showCorrectButtons() {

        switch (viewPager.getCurrentItem()) {
            case STOPWATCH_NUMBER:
                if (!isAddButtonStopwatchClicked) {
                    showAddListButton();
                    setInvisible();
                } else {
                    showAddTimeButton();
                    setRecyclerViewAdaperStopwatch();
                    setVisible();
                }
                break;
            case TIMER_NUMBER:
                if (!isAddButtonTimerClicked) {
                    showAddListButton();
                    setInvisible();
                } else {
                    showAddTimeButton();
                    setRecyclerViewAdaperTimer();
                    setVisible();
                }
                break;
        }
    }

    public void setStopwatchService(StopwatchService stopwatchService) {
        this.stopwatchService = stopwatchService;
    }

    public void setTimerService(TimerService timerService) {
        this.timerService = timerService;
    }
}