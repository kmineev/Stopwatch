package com.spaceotechnologies.training.stopwatch.base;

/**
 * Created by Kostez on 24.07.2016.
 */
public class Timer {

    public long getStartTime() {
        return startTime;
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

    public enum State {
        PAUSED, RUNNING, BINDED
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

    public Timer(long startTime) {
        time = systemTime;
        reset(startTime);
    }

    public Timer(GetTime time) {
        this.time = time;
        reset();
    }

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

    public void reset(long startTime) {
        state = State.BINDED;
        this.startTime = startTime;
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
