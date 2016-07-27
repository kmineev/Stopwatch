package com.spaceotechnologies.training.stopwatch.Base;

/**
 * Created by Kostez on 24.07.2016.
 */
public class Timer {

    public interface GetTime {
        public long now();
    }

    private GetTime systemTime = new GetTime() {
        @Override
        public long now() {
            return System.currentTimeMillis();
        }
    };

    public enum State {
        PAUSED, RUNNING
    }

    private GetTime time;
    private long startTime;
    private long stopTime;
    private long pauseOffset;
    private State state;

    public Timer() {
        time = systemTime;
        reset();
    }

    public Timer(GetTime time) {
        time = time;
        reset();
    }

    public void start() {
        if (state == State.PAUSED) {
            pauseOffset = getElapsedTime();
            stopTime = 0;
            startTime = time.now();
            state = State.RUNNING;
        }
    }

    public void pause() {
        if (state == State.RUNNING) {
            stopTime = time.now();
            state = State.PAUSED;
        }
    }

    public void reset() {
        state = State.PAUSED;
        startTime = 0;
        stopTime = 0;
        pauseOffset = 0;
    }

    public long getElapsedTime() {
        if (state == State.PAUSED) {
            return (stopTime - startTime) + pauseOffset;
        } else {
            return (time.now() - startTime) + pauseOffset;
        }
    }

    public boolean isRunning() {
        return (state == State.RUNNING);
    }
}
