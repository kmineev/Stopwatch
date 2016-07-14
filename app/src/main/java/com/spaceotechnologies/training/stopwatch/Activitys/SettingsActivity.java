package com.spaceotechnologies.training.stopwatch.Activitys;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.spaceotechnologies.training.stopwatch.Fragments.SettingsFragment;
import com.spaceotechnologies.training.stopwatch.R;

public class SettingsActivity extends AppCompatActivity {

    SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            settingsFragment = new SettingsFragment();
            fragmentTransaction.add(R.id.settings_content, settingsFragment);
            fragmentTransaction.commit();
        }
    }
}