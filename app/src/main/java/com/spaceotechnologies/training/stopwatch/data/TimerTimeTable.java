package com.spaceotechnologies.training.stopwatch.data;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Kostez on 25.08.2016.
 */
public class TimerTimeTable {

    @DatabaseField(generatedId = true, columnName = "timer_time_id")
    private int timerTimeId;

    @DatabaseField(columnName = "timer_time_value")
    private String timerTimeValue;

    public TimerTimeTable() {

    }

    public TimerTimeTable(String timerTimeValue) {
        this.timerTimeValue = timerTimeValue;
    }

    public int getTimerTimeId() {
        return timerTimeId;
    }

    public String getTimerTimeValue() {
        return timerTimeValue;
    }

    public void setTimerTimeValue(String timerTimeValue) {
        this.timerTimeValue = timerTimeValue;
    }
}
