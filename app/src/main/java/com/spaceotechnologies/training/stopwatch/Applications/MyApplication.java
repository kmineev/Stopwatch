package com.spaceotechnologies.training.stopwatch.applications;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.spaceotechnologies.training.stopwatch.data.DatabaseHelperFactory;

/**
 * Created by Kostez on 01.08.2016.
 */
public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();

    private static Context context;
    private static SharedPreferences preferences;
    private static final String PREFERENCES = "preferences";

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        preferences = getSharedPreferences( getPackageName() + PREFERENCES, MODE_PRIVATE);
        DatabaseHelperFactory.setHelper(getApplicationContext());
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static SharedPreferences getPreferences() {
        return preferences;
    }

    @Override
    public void onTerminate() {
        DatabaseHelperFactory.releaseHelper();
        super.onTerminate();
    }
}