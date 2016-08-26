package com.spaceotechnologies.training.stopwatch.data;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Kostez on 25.08.2016.
 */
public class StopwatchTimeTable implements Serializable {

    public static final String ID_FIELD = "stopwatch_time_id";
    public static final String VALUE_FIELD = "stopwatch_time_value";

    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    private int stopwatchTimeId;

    @DatabaseField(columnName = VALUE_FIELD)
    private String stopwatchTimeValue;

    public StopwatchTimeTable() {

    }

    public StopwatchTimeTable(String stopwatchTimeValue) {
        this.stopwatchTimeValue = stopwatchTimeValue;
    }

    public int getStopwatchTimeId() {
        return stopwatchTimeId;
    }

    public String getStopwatchTimeValue() {
        return stopwatchTimeValue;
    }

    public void setStopwatchTimeValue(String stopwatchTimeValue) {
        this.stopwatchTimeValue = stopwatchTimeValue;
    }
}
