package com.spaceotechnologies.training.stopwatch.Applications;

import android.app.Application;
import android.content.Context;

/**
 * Created by Kostez on 01.08.2016.
 */
public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}