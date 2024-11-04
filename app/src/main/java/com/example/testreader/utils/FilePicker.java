package com.example.testreader.utils;

import android.app.Activity;

import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.Fragment;

public class FilePicker {
    private static final int PICK_FILE_REQUEST_CODE = 1;

    // 从 Activity 启动文件选择器
    public static void openFilePicker(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/zip"); // 设置文件类型为 ZIP（CBZ 是 ZIP 格式）
        intent.addCategory(Intent.CATEGORY_OPENABLE); // 允许打开文件
        activity.startActivityForResult(intent, PICK_FILE_REQUEST_CODE); // 启动文件选择器
    }

    // 从 Fragment 启动文件选择器
    public static void openFilePicker(Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/zip");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        fragment.startActivityForResult(intent, PICK_FILE_REQUEST_CODE); // 从 Fragment 启动文件选择器
    }

        // 处理文件选择结果并返回 URI
    public static Uri handleFileResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            return data.getData(); // 返回选择的文件 URI
        }
        return null;
    }



}



//
//import android.app.Activity;
//import android.content.Intent;
//
//public class FilePicker {
//    private static final int PICK_FILE_REQUEST_CODE = 1;
//
//    // 启动文件选择器
//    public static void openFilePicker(Activity activity) {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("application/zip"); // 设置文件类型为 ZIP（CBZ 是 ZIP 格式）
//        intent.addCategory(Intent.CATEGORY_OPENABLE); // 允许打开文件
//        activity.startActivityForResult(intent, PICK_FILE_REQUEST_CODE); // 启动文件选择器
//    }
//}
