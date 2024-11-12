package com.example.testreader.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfToImageConverter {

    // 将 PDF 文件转换为图像
    public static void convertPdfToImages(Uri pdfUri, ProgressBar progressBar, Context context, PdfConversionCallback callback) {
        // 这个函数负责PDF转换，并在进度条上显示进度
        new ConvertTask(pdfUri, progressBar, context, callback).execute();
    }


    // 定义回调接口
    public interface PdfConversionCallback {
        void onConversionComplete(File outputDir, String pdfFileName);
    }

    // 异步任务，用于PDF转换
    private static class ConvertTask extends AsyncTask<Void, Integer, Void> {
        private Uri pdfUri;
        private ProgressBar progressBar;
        private Context context;
        private PdfConversionCallback callback;  // 回调对象
//        public ConvertTask(Uri pdfUri, ProgressBar progressBar, Context context) {
//            this.pdfUri = pdfUri;
//            this.progressBar = progressBar;
//            this.context = context;
//        }
        public ConvertTask(Uri pdfUri, ProgressBar progressBar, Context context, PdfConversionCallback callback) {
            this.pdfUri = pdfUri;
            this.progressBar = progressBar;
            this.context = context;
            this.callback = callback;  // 赋值回调对象
}

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // 使用传递进来的 context 来获取 ContentResolver
                ParcelFileDescriptor fileDescriptor = context.getContentResolver().openFileDescriptor(pdfUri, "r");
                PdfRenderer renderer = new PdfRenderer(fileDescriptor);

                // 获取 PDF 文件名称
                String pdfFileName = getFileNameFromUri(pdfUri);

                // 使用 PDF 文件名称作为文件夹名称
                File outputDir = new File(context.getExternalFilesDir(null), pdfFileName + "_images");
                if (!outputDir.exists()) {
                    outputDir.mkdirs();  // 如果目录不存在则创建
                }

                int pageCount = renderer.getPageCount();

                // 初始化进度条
                progressBar.setMax(pageCount);
                progressBar.setProgress(0);

                // 渲染每一页并保存为图像
                for (int i = 0; i < pageCount; i++) {
                    PdfRenderer.Page page = renderer.openPage(i);
                    Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                    // 保存图片
                    File imageFile = new File(outputDir, "page_" + (i + 1) + ".png");
                    Log.d("PdfToImage", "Rendering page " + (i + 1));
                    Log.d("PdfToImage", "Saving image to " + imageFile.getAbsolutePath());
                    FileOutputStream out = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.close();
                    page.close();

                    // 更新进度条
                    publishProgress(i + 1);
                }

                renderer.close();
                fileDescriptor.close();

                // 压缩图片文件夹
                //ZipUtils.zipDirectory(outputDir, pdfFileName + ".zip");
                // 提示用户压缩完成
                callback.onConversionComplete(outputDir, pdfFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
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
            return result != null ? result.replace(".pdf", "") : "converted_images";
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
            //super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);  // 显示进度条
            progressBar.setProgress(0);               // 初始化进度
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setProgress(progressBar.getMax()); // 设置进度条到满
            progressBar.setVisibility(View.GONE);          // 隐藏进度条
        }
    }
}
