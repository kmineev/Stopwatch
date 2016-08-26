package com.spaceotechnologies.training.stopwatch.data;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Kostez on 25.08.2016.
 */
public class CutoffTimerTable {

    public static final String ID_FIELD = "cutoff_timer_id";
    public static final String VALUE_FIELD = "cutoff_timer_value";


    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    public int cutoffTimerId;

    @DatabaseField(columnName = VALUE_FIELD)
    public String cutoffTimerValue;

    public CutoffTimerTable() {

    }

    public CutoffTimerTable(String cutoffTimerValue) {
        this.cutoffTimerValue = cutoffTimerValue;
    }
}
