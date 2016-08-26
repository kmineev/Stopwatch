package com.spaceotechnologies.training.stopwatch.data;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Kostez on 25.08.2016.
 */
public class TimerTimeTable {

    public static final String ID_FIELD = "timer_time_id";
    public static final String VALUE_FIELD = "timer_time_value";


    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    private int timerTimeId;

    @DatabaseField(columnName = VALUE_FIELD)
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
