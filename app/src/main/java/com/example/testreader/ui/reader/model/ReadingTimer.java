package com.example.testreader.ui.reader.model;

import android.os.Handler;
import android.util.Log;

public class ReadingTimer {

    private static final String TAG = "ReadingTimer";
    private int secondsElapsed = 0;
    private boolean isRunning = false;
    private Handler handler;
    private Runnable timeUpdater;

    public ReadingTimer() {
        handler = new Handler();
        timeUpdater = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    secondsElapsed++;
                    Log.d(TAG, "Seconds elapsed: " + secondsElapsed); // 日志：显示计时秒数
                    handler.postDelayed(this, 1000); // 每秒更新一次
                }
            }
        };
    }

    // 开始计时
    public void start() {
        if (!isRunning) {
            isRunning = true;
            handler.post(timeUpdater);
            Log.d(TAG, "Timer started"); // 日志：计时开始
        }
    }

    // 停止计时
    public void stop() {
        isRunning = false;
        handler.removeCallbacks(timeUpdater);
        Log.d(TAG, "Timer stopped"); // 日志：计时停止
    }

    // 获取已阅读秒数
    public int getSecondsElapsed() {
        return secondsElapsed;
    }
}
