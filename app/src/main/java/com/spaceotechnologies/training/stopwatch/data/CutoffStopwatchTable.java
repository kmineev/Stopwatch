package com.spaceotechnologies.training.stopwatch.data;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Kostez on 25.08.2016.
 */
public class CutoffStopwatchTable {

    @DatabaseField(generatedId = true, columnName = "cutoff_stopwatch_id")
    public int cutoffStopwatchId;

    @DatabaseField(columnName = "cutoff_stopwatch_value")
    public String cutoffStopwatchValue;

    public CutoffStopwatchTable() {

    }

    public CutoffStopwatchTable(String cutoffStopwatchValue) {
        this.cutoffStopwatchValue = cutoffStopwatchValue;
    }
}
