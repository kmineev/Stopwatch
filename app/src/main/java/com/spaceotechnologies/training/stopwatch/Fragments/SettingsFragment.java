package com.spaceotechnologies.training.stopwatch.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.activitys.ColorsActivity;
import com.spaceotechnologies.training.stopwatch.activitys.PicturesActivity;
import com.spaceotechnologies.training.stopwatch.applications.MyApplication;

/**
 * Created by Kostez on 23.08.2016.
 */
public class SettingsFragment extends PreferenceFragment {

    CheckBoxPreference saveCutoffCheckBoxPreference;
    Preference colorsSelectionPreference;
    Preference backgroundSelectionPreference;

    private static final int REQUEST_CODE = 1;
    public static final String SAVE_CUTOFF = "saveCutoff";
    public static final String COLORS_SELECTION = "colors_selection";
    public static final String BACKGROUND_SELECTION = "background_selection";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        saveCutoffCheckBoxPreference = (CheckBoxPreference) findPreference(SAVE_CUTOFF);
        colorsSelectionPreference = findPreference(COLORS_SELECTION);
        backgroundSelectionPreference = findPreference(BACKGROUND_SELECTION);

        colorsSelectionPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(MyApplication.getAppContext(), ColorsActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                return true;
            }
        });

        backgroundSelectionPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(MyApplication.getAppContext(), PicturesActivity.class);
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
