package com.spaceotechnologies.training.stopwatch.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.activitys.ColorsActivity;
import com.spaceotechnologies.training.stopwatch.applications.MyApplication;

/**
 * Created by Kostez on 23.08.2016.
 */
public class SettingsFragment extends PreferenceFragment {

    CheckBoxPreference saveCutoffCheckBoxPreference;
    Preference colorsSelectionPreference;
    private static final int REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        saveCutoffCheckBoxPreference = (CheckBoxPreference)findPreference(MyApplication.getAppContext().getResources().getString(R.string.save_cutoff));
        colorsSelectionPreference = (Preference)findPreference(MyApplication.getAppContext().getResources().getString(R.string.colors_selection));

        colorsSelectionPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(MyApplication.getAppContext(), ColorsActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getActivity().setResult(Activity.RESULT_OK, data);
    }
}
