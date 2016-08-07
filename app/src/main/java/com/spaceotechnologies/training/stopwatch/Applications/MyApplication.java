package com.spaceotechnologies.training.stopwatch.Applications;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kostez on 01.08.2016.
 */
public class MyApplication extends Application {

    private static Context context;
    private static SharedPreferences preferences;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        preferences = getSharedPreferences( getPackageName() + "_preferences", MODE_PRIVATE);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static SharedPreferences getPreferences() {
        return preferences;
    }
}