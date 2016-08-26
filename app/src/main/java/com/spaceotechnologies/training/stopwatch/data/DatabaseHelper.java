package com.spaceotechnologies.training.stopwatch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Kostez on 25.08.2016.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "mydatabasedb.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<StopwatchTimeTable, Integer> stopwatchTimeTableDao;
    private Dao<TimerTimeTable, Integer> timerTimeTableDao;
    private Dao<ColorTable, Integer> colorTableDao;
    private Dao<CutoffStopwatchTable, Integer> cutoffStopwatchTableDao;
    private Dao<CutoffTimerTable, Integer> cutoffTimerTableDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, StopwatchTimeTable.class);
            TableUtils.createTable(connectionSource, TimerTimeTable.class);
            TableUtils.createTable(connectionSource, ColorTable.class);
            TableUtils.createTable(connectionSource, CutoffStopwatchTable.class);
            TableUtils.createTable(connectionSource, CutoffTimerTable.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, StopwatchTimeTable.class, true);
            TableUtils.dropTable(connectionSource, TimerTimeTable.class, true);
            TableUtils.dropTable(connectionSource, ColorTable.class, true);
            TableUtils.dropTable(connectionSource, CutoffStopwatchTable.class, true);
            TableUtils.dropTable(connectionSource, CutoffTimerTable.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVersion + " to new "
                    + newVersion, e);
        }
    }

    public Dao<StopwatchTimeTable, Integer> getStopwatchTimeTableDao() throws SQLException {
        if (stopwatchTimeTableDao == null) {
            stopwatchTimeTableDao = getDao(StopwatchTimeTable.class);
        }
        return stopwatchTimeTableDao;
    }

    public Dao<TimerTimeTable, Integer> getTimerTimeTableDao() throws SQLException {
        if (timerTimeTableDao == null) {
            timerTimeTableDao = getDao(TimerTimeTable.class);
        }
        return timerTimeTableDao;
    }

    public Dao<ColorTable, Integer> getColorTableDao() throws SQLException {
        if (colorTableDao == null) {
            colorTableDao = getDao(ColorTable.class);
        }
        return colorTableDao;
    }

    public Dao<CutoffStopwatchTable, Integer> getCutoffStopwatchTableDao() throws SQLException {
        if (cutoffStopwatchTableDao == null) {
            cutoffStopwatchTableDao = getDao(CutoffStopwatchTable.class);
        }
        return cutoffStopwatchTableDao;
    }

    public Dao<CutoffTimerTable, Integer> getCutoffTimerTableDao() throws SQLException {
        if (cutoffTimerTableDao == null) {
            cutoffTimerTableDao = getDao(CutoffTimerTable.class);
        }
        return cutoffTimerTableDao;
    }

}
