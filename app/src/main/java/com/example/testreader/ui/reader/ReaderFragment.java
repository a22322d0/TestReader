package com.example.testreader.ui.reader;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testreader.R;
import com.example.testreader.ui.reader.model.ReaderModeManager;
import com.example.testreader.ui.reader.model.ReadingTimer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReaderFragment extends Fragment {

    private static final String TAG = "ReaderFragment";
    private Button modeSwitchButton;
    private ViewGroup readerContainer;
    private List<File> imageFiles;
    private ReaderModeManager modeManager;
    private ReadingTimer readingTimer;
    private TextView timeDisplay;

    public ReaderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reader, container, false);

        // 初始化视图组件
        readerContainer = rootView.findViewById(R.id.readerContainer);
        modeSwitchButton = rootView.findViewById(R.id.modeSwitchButton);
        timeDisplay = rootView.findViewById(R.id.timeDisplay); // 显示时间的 TextView
        //log 测试
        if (timeDisplay == null) {
            Log.d("ReaderFragment", "timerTextView is null. Check XML layout file.");
        } else {
            Log.d("ReaderFragment", "timerTextView successfully referenced.");
        }
        // 获取漫画图片文件列表
        imageFiles = getComicPages();
        Log.d(TAG, "Image files loaded: " + imageFiles.size());

        // 初始化阅读模式管理器
        modeManager = new ReaderModeManager(requireContext(), readerContainer, imageFiles);
        modeManager.setupMode(ReaderModeManager.MODE_SINGLE_PAGE_LEFT_TO_RIGHT);
        Log.d(TAG, "Reader mode set up");

        // 初始化时间处理器并启动计时
        readingTimer = new ReadingTimer();
        readingTimer.start();

        // 每秒更新 UI 中的时间显示
        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int secondsElapsed = readingTimer.getSecondsElapsed();
                Log.d(TAG, "Updating UI with elapsed time: " + secondsElapsed);
                timeDisplay.setText("已阅读: " + secondsElapsed + " 秒");
                rootView.postDelayed(this, 1000); // 每秒更新一次
            }
        }, 1000);

        // 切换模式按钮点击事件
        modeSwitchButton.setOnClickListener(v -> modeManager.switchMode());

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 停止计时
        if (readingTimer != null) {
            readingTimer.stop();
            Log.d(TAG, "Timer stopped in onDestroyView");
        }
    }

    private List<File> getComicPages() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            Log.d(TAG, "Arguments received for comic pages");
            return (List<File>) arguments.getSerializable("imageFiles");
        }
        Log.d(TAG, "No arguments received, returning empty list");
        return new ArrayList<>();
    }

    public void updateTimerUI(int secondsElapsed) {
        if (timeDisplay != null) {
            timeDisplay.setText("Elapsed time: " + secondsElapsed + "s");
            Log.d("ReaderFragment", "Updated TextView with time: " + secondsElapsed + "s");
        } else {
            Log.d("ReaderFragment", "timerTextView is null during update.");
        }
    }
}
