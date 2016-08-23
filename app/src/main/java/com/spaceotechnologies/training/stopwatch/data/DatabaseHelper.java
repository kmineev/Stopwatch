package com.spaceotechnologies.training.stopwatch.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.spaceotechnologies.training.stopwatch.R;
import com.spaceotechnologies.training.stopwatch.applications.MyApplication;

/**
 * Created by Kostez on 18.08.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns{

    // версия базы данных
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_SCRIPT_STOPWATCH = "create table "
            + MyApplication.getAppContext().getResources().getString(R.string.stopwatch_time_table) + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + MyApplication.getAppContext().getResources().getString(R.string.time_name_column)
            + " text not null);";

    private static final String DATABASE_CREATE_SCRIPT_TIMER = "create table "
            + MyApplication.getAppContext().getResources().getString(R.string.timer_time_table) + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + MyApplication.getAppContext().getResources().getString(R.string.time_name_column)
            + " text not null);";

    private static final String DATABASE_CREATE_SCRIPT_COLORS = "create table "
            + MyApplication.getAppContext().getResources().getString(R.string.colors_table) + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + MyApplication.getAppContext().getResources().getString(R.string.color_name_column)
            + " text not null, " + MyApplication.getAppContext().getResources().getString(R.string.color_value_column)
            + " text not null);";

    private static final String DATABASE_CREATE_SCRIPT_CUTOFF_STOPWATCH = "create table "
            + MyApplication.getAppContext().getResources().getString(R.string.cutoff_stopwatch_table) + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + MyApplication.getAppContext().getResources().getString(R.string.cutoff_value_value)
            + " text not null);";

    private static final String DATABASE_CREATE_SCRIPT_CUTOFF_TIMER = "create table "
            + MyApplication.getAppContext().getResources().getString(R.string.cutoff_timer_table) + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + MyApplication.getAppContext().getResources().getString(R.string.cutoff_value_value)
            + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, MyApplication.getAppContext().getResources().getString(R.string.database_name), null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_SCRIPT_STOPWATCH);
        sqLiteDatabase.execSQL(DATABASE_CREATE_SCRIPT_TIMER);
        sqLiteDatabase.execSQL(DATABASE_CREATE_SCRIPT_COLORS);
        sqLiteDatabase.execSQL(DATABASE_CREATE_SCRIPT_CUTOFF_STOPWATCH);
        sqLiteDatabase.execSQL(DATABASE_CREATE_SCRIPT_CUTOFF_TIMER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + MyApplication.getAppContext().getResources().getString(R.string.stopwatch_time_table));
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + MyApplication.getAppContext().getResources().getString(R.string.timer_time_table));
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + MyApplication.getAppContext().getResources().getString(R.string.colors_table));
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + MyApplication.getAppContext().getResources().getString(R.string.cutoff_stopwatch_table));
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS " + MyApplication.getAppContext().getResources().getString(R.string.cutoff_timer_table));

        // Создаём новую таблицу
        onCreate(sqLiteDatabase);
    }
}
