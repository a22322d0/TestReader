package com.example.testreader.ui.reader.model;

import android.content.Context;
import android.os.Handler;

public class TimerModel {

    private long startTime;
    private long elapsedTime;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private boolean isRunning;
    private TimerListener listener;

    public interface TimerListener {
        void onTimeUpdated(long timeInSeconds);
    }

    public TimerModel(Context context, TimerListener listener) {
        this.listener = listener;
        this.timerHandler = new Handler();
        this.timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    elapsedTime = (System.currentTimeMillis() - startTime) / 1000;  // seconds
                    listener.onTimeUpdated(elapsedTime);
                    timerHandler.postDelayed(this, 1000);  // Update every second
                }
            }
        };
    }

    // Start the timer
    public void start() {
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
        isRunning = true;
        timerHandler.post(timerRunnable);
    }

    // Stop the timer
    public void stop() {
        isRunning = false;
    }

    // Get the current elapsed time in seconds
    public long getElapsedTime() {
        return elapsedTime;
    }
}
