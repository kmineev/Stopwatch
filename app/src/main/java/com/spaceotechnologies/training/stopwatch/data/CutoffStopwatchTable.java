package com.spaceotechnologies.training.stopwatch.data;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Kostez on 25.08.2016.
 */
public class CutoffStopwatchTable {

    public static final String ID_FIELD = "cutoff_stopwatch_id";
    public static final String VALUE_FIELD = "cutoff_stopwatch_value";

    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    public int cutoffStopwatchId;

    @DatabaseField(columnName = VALUE_FIELD)
    public String cutoffStopwatchValue;

    public CutoffStopwatchTable() {

    }

    public CutoffStopwatchTable(String cutoffStopwatchValue) {
        this.cutoffStopwatchValue = cutoffStopwatchValue;
    }
}
