package com.spaceotechnologies.training.stopwatch.data;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Kostez on 25.08.2016.
 */
public class StopwatchTimeTable implements Serializable {

    @DatabaseField(generatedId = true, columnName = "stopwatch_time_id")
    private int stopwatchTimeId;

    @DatabaseField(columnName = "stopwatch_time_value")
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
