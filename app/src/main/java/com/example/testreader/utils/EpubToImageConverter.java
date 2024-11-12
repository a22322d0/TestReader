package com.example.testreader.utils;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class EpubToImageConverter {

    // 将 EPUB 文件转换为图像
    public static void convertEpubToImages(Uri epubUri, ProgressBar progressBar, Context context, EpubConversionCallback callback) {
        new ConvertTask(epubUri, progressBar, context, callback).execute();
    }

    // 定义回调接口
    public interface EpubConversionCallback {
        void onConversionComplete(File outputDir, String epubFileName);
    }

    // 异步任务，用于EPUB转换
    private static class ConvertTask extends AsyncTask<Void, Integer, Void> {
        private Uri epubUri;
        private ProgressBar progressBar;
        private Context context;
        private EpubConversionCallback callback;

        public ConvertTask(Uri epubUri, ProgressBar progressBar, Context context, EpubConversionCallback callback) {
            this.epubUri = epubUri;
            this.progressBar = progressBar;
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // 获取 EPUB 文件名称
                String epubFileName = getFileNameFromUri(epubUri);

                // 创建输出目录
                File outputDir = new File(context.getExternalFilesDir(null), epubFileName + "_images");
                if (!outputDir.exists()) {
                    outputDir.mkdirs();
                }

                // 创建临时目录用于解压EPUB文件
                File tempDir = new File(context.getCacheDir(), epubFileName + "_temp");
                if (!tempDir.exists()) {
                    tempDir.mkdirs();
                }

                // 将EPUB解压到临时目录
                File epubFile = new File(tempDir, epubFileName + ".epub");
                //context.getContentResolver().openInputStream(epubUri).transferTo(new FileOutputStream(epubFile));
                try (InputStream inputStream = context.getContentResolver().openInputStream(epubUri);
                     OutputStream outputStream = new FileOutputStream(epubFile)) {

                    copyStream(inputStream, outputStream);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                ZipFile zipFile = new ZipFile(epubFile);
                zipFile.extractAll(tempDir.getAbsolutePath());

                // 查找图片文件
                List<File> images = new ArrayList<>();
                findImages(tempDir, images);

                // 设置进度条
                progressBar.setMax(images.size());
                progressBar.setProgress(0);

                // 复制所有图片到输出目录
                for (int i = 0; i < images.size(); i++) {
                    File imageFile = new File(outputDir, "page_" + (i + 1) + ".png");
                    Log.d("EpubToImage", "Copying image: " + images.get(i).getAbsolutePath() + " to " + imageFile.getAbsolutePath());
                    images.get(i).renameTo(imageFile);
                    publishProgress(i + 1);
                }

                // 调用回调
                callback.onConversionComplete(outputDir, epubFileName);

                // 删除临时目录
                deleteDirectory(tempDir);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        // 复制输入流到输出流，兼容较低的 API 版本
        public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        }

        // 递归查找包含图片文件的文件夹
        private void findImages(File dir, List<File> imageFiles) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    findImages(file, imageFiles);  // 递归子文件夹
                } else if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg") || file.getName().endsWith(".png")) {
                    imageFiles.add(file);  // 将图片文件添加到列表中
                }
            }
        }

        // 获取文件名的辅助方法
        private String getFileNameFromUri(Uri uri) {
            String result = null;
            if (uri.getScheme().equals("content")) {
                try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (index >= 0) {
                            result = cursor.getString(index);
                        }
                    }
                }
            }
            if (result == null) {
                result = uri.getPath();
                int cut = result.lastIndexOf('/');
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
            return result != null ? result.replace(".epub", "") : "converted_images";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values.length > 0) {
                progressBar.setProgress(values[0]);
            }
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setProgress(progressBar.getMax());
            progressBar.setVisibility(View.GONE);
        }

        // 删除文件夹及其内容的辅助方法
        private void deleteDirectory(File dir) {
            if (dir.isDirectory()) {
                for (File child : dir.listFiles()) {
                    deleteDirectory(child);
                }
            }
            dir.delete();
        }
    }
}

