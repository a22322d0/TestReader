package com.example.testreader.utils;

import android.content.Intent;
import android.net.Uri;

public class FilePicker {

    public static Intent createFilePickerIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/zip"); // 根据你想要允许的文件类型进行调整
        return Intent.createChooser(intent, "选择CBZ文件");
    }

    public static Uri handleFileResult(Intent data) {
        return data.getData();
    }
}
