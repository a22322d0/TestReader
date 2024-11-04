package com.example.testreader.utils;

//public class CbzExtractor {
//}



//import android.content.Context;
//import android.net.Uri;
//import android.util.Log;
//import net.lingala.zip4j.ZipFile;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import android.content.Context;
//import android.net.Uri;
//import android.util.Log;
//
//import net.lingala.zip4j.ZipFile;
//
//import java.io.InputStream;
//
//public class CbzExtractor {
//    private Context context;
//
//    public CbzExtractor(Context context) {
//        this.context = context;
//    }
//
//    public void extract(Uri fileUri, String destinationDirectory) {
//        try (InputStream inputStream = context.getContentResolver().openInputStream(fileUri)) {
//            if (inputStream != null) {
//                ZipFile zipFile = new ZipFile(inputStream);
//                zipFile.extractAll(destinationDirectory);
//            } else {
//                Log.e("CbzExtractor", "Input stream is null");
//            }
//        } catch (Exception e) {
//            Log.e("CbzExtractor", "Failed to extract CBZ file", e);
//        }
//    }
//}


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CbzExtractor {
    private static final String TAG = "CbzExtractor";

    // 解压 CBZ 文件到指定目录，返回解压出的图片文件列表
    public static List<File> extract(Uri cbzUri, File destinationDir, Context context) {
        List<File> extractedFiles = new ArrayList<>();
        InputStream inputStream = null;

        try {
            // 使用 ContentResolver 获取输入流
            inputStream = context.getContentResolver().openInputStream(cbzUri);
            if (inputStream != null) {
                // 创建临时文件以解压
                File tempFile = new File(destinationDir, "temp.zip");
                try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                }

                // 使用 ZipFile 解压
                ZipFile zipFile = new ZipFile(tempFile);
                zipFile.extractAll(destinationDir.getAbsolutePath());

                // 筛选图片文件
                for (File file : destinationDir.listFiles()) {
                    if (file.isFile() && isImageFile(file)) {
                        extractedFiles.add(file);
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to extract CBZ file", e);
        } finally {
            // 关闭输入流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Failed to close input stream", e);
                }
            }
        }
        return extractedFiles;
    }

    // 辅助方法：判断文件是否为图片类型
    private static boolean isImageFile(File file) {
        String[] imageExtensions = { ".jpg", ".jpeg", ".png", ".gif" };
        for (String extension : imageExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}


//
//public class CbzExtractor {
//    private static final String TAG = "CbzExtractor";
//
//    // 解压 CBZ 文件到指定目录，返回解压出的图片文件列表
//    public static List<File> extract(Uri cbzUri, File destinationDir, Context context) {
//        List<File> extractedFiles = new ArrayList<>();
//        try {
//            File cbzFile = new File(cbzUri.getPath()); // 假设已得到路径
//            ZipFile zipFile = new ZipFile(cbzFile);
//
//            // 解压到目标文件夹
//            zipFile.extractAll(destinationDir.getAbsolutePath());
//
//            // 筛选图片文件
//            for (File file : destinationDir.listFiles()) {
//                if (file.isFile() && isImageFile(file)) {
//                    extractedFiles.add(file);
//                }
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "Failed to extract CBZ file", e);
//        }
//        return extractedFiles;
//    }
//
//    // 辅助方法：判断文件是否为图片类型
//    private static boolean isImageFile(File file) {
//        String[] imageExtensions = { ".jpg", ".jpeg", ".png", ".gif" };
//        for (String extension : imageExtensions) {
//            if (file.getName().toLowerCase().endsWith(extension)) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
