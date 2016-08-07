package com.spaceotechnologies.training.stopwatch.Base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kostez on 21.07.2016.
 */
public class Stopwatch {

    public long getPauseOffset() {
        return pauseOffset;
    }

    public interface GetTime {
        public long now();
    }

    private GetTime systemTime = new GetTime() {
        @Override
        public long now() {
            return System.currentTimeMillis();
        }
    };

    /**
     * What is the stopwatch doing?
     */
    public enum State {
        PAUSED, RUNNING, BINDED
    }

    private GetTime time;
    private long startTime;
    private long stopTime;
    private long pauseOffset;
    private List<Long> laps = new ArrayList<Long>();
    private State state;

    public Stopwatch() {
        time = systemTime;
        reset();
    }

    public Stopwatch(long startTime) {
        time = systemTime;
        reset(startTime);
    }

    public Stopwatch(GetTime time) {
        this.time = time;
        reset();
    }

    /**
     * Start the stopwatch running. If the stopwatch is already running, this
     * does nothing.
     */
    public void start() {
        if (state == State.PAUSED) {
            pauseOffset = getElapsedTime();
            stopTime = 0;
            startTime = time.now();
            state = State.RUNNING;
        } else if (state == State.BINDED) {
            pauseOffset = 0;
            stopTime = 0;
            state = State.RUNNING;
        }
    }

    /***
     * Pause the stopwatch. If the stopwatch is already running, do nothing.
     */
    public void pause() {
        if (state == State.RUNNING) {
            stopTime = time.now();
            state = State.PAUSED;
        }
    }

    /**
     * Reset the stopwatch to the initial state, clearing all stored times.
     */
    public void reset() {
        state = State.PAUSED;
        startTime = 0;
        stopTime = 0;
        pauseOffset = 0;
        laps.clear();
    }

    public void reset(long startTime) {
        state = State.BINDED;
        this.startTime = startTime;
        stopTime = 0;
        this.pauseOffset = 0;
    }

    /***
     * @return The amount of time recorded by the stopwatch, in milliseconds
     */
    public long getElapsedTime() {
        if (state == State.PAUSED) {
            return (stopTime - startTime) + pauseOffset;
        } else {
            return (time.now() - startTime) + pauseOffset;
        }
    }

    /**
     * @return true if the stopwatch is currently running and recording
     * time, false otherwise.
     */
    public boolean isRunning() {
        return (state == State.RUNNING);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }



}
