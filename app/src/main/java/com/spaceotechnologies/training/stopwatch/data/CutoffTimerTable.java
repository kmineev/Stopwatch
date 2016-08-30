package com.spaceotechnologies.training.stopwatch.data;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Kostez on 25.08.2016.
 */
public class CutoffTimerTable {

    @DatabaseField(generatedId = true, columnName = "cutoff_timer_id")
    public int cutoffTimerId;

    @DatabaseField(columnName = "cutoff_timer_value")
    public String cutoffTimerValue;

    public CutoffTimerTable() {

    }

    public CutoffTimerTable(String cutoffTimerValue) {
        this.cutoffTimerValue = cutoffTimerValue;
    }
}
