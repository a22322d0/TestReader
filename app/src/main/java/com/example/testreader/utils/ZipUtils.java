package com.example.testreader.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    private static final String TAG = "ZipUtils";  // Log 标签

    // 定义一个接口，用于通知压缩结果
    public interface ZipCompletionListener {
        void onZipCompleted(String zipFilePath);
        void onZipFailed(String errorMessage);
    }

    // 压缩文件夹并在压缩完成后删除原始文件夹
    public static void zipDirectory(File directory, String zipFileName, ZipCompletionListener listener) {
        File zipFile = new File(directory.getParent(), zipFileName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
             ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {

            Log.d(TAG, "==BEGIN==" + directory.getAbsolutePath());
            zipDirectoryRecursively(directory, directory.getName(), zipOutputStream);
            Log.d(TAG, "==FINISH== zip_file_path:" + zipFile.getAbsolutePath());

            // 压缩完成后删除原始文件夹
            deleteDirectoryRecursively(directory);

            // 调用成功回调
            if (listener != null) {
                listener.onZipCompleted(zipFile.getAbsolutePath());
            }

        } catch (IOException e) {
            Log.e(TAG, "XX==FAIL==XX", e);
            // 调用失败回调
            if (listener != null) {
                listener.onZipFailed(e.getMessage());
            }
        }
    }

    // 递归压缩文件夹内容
    private static void zipDirectoryRecursively(File directory, String parentDirectoryName, ZipOutputStream zipOutputStream) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归处理子目录
                    Log.d(TAG, "child_dir" + file.getAbsolutePath());
                    zipDirectoryRecursively(file, parentDirectoryName + "/" + file.getName(), zipOutputStream);
                } else {
                    // 压缩文件
                    Log.d(TAG, "zip_file:" + file.getAbsolutePath());
                    try (FileInputStream fileInputStream = new FileInputStream(file)) {
                        String entryName = parentDirectoryName + "/" + file.getName();
                        ZipEntry zipEntry = new ZipEntry(entryName);
                        zipOutputStream.putNextEntry(zipEntry);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fileInputStream.read(buffer)) >= 0) {
                            zipOutputStream.write(buffer, 0, length);
                        }

                        zipOutputStream.closeEntry();
                        Log.d(TAG, "files_had_zip_path:" + file.getAbsolutePath());
                    }
                }
            }
        }
    }

    // 递归删除文件夹内容
    private static void deleteDirectoryRecursively(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectoryRecursively(file);  // 递归删除子目录
                } else {
                    if (file.delete()) {
                        Log.d(TAG, "Deleted file: " + file.getAbsolutePath());
                    } else {
                        Log.e(TAG, "Failed to delete file: " + file.getAbsolutePath());
                    }
                }
            }
        }
        if (directory.delete()) {
            Log.d(TAG, "Deleted directory: " + directory.getAbsolutePath());
        } else {
            Log.e(TAG, "Failed to delete directory: " + directory.getAbsolutePath());
        }
    }
}
