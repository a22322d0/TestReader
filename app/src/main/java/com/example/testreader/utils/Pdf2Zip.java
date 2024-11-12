package com.example.testreader.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Pdf2Zip {

    public static void processPdf(Uri pdfUri, ProgressBar progressBar, Context context) {
        // 将 PDF 转换为图像
        PdfToImageConverter.convertPdfToImages(pdfUri, progressBar, context, (outputDir, pdfFileName) -> {
            // 转换完成后，将图片压缩成 zip 文件
            ZipUtils.zipDirectory(outputDir, pdfFileName + ".zip", new ZipUtils.ZipCompletionListener() {
                @Override
                public void onZipCompleted(String zipFilePath) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "导入完成，保存路径：" + zipFilePath, Toast.LENGTH_SHORT).show()
                    );
                }

                @Override
                public void onZipFailed(String errorMessage) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            Toast.makeText(context, "导入失败：" + errorMessage, Toast.LENGTH_LONG).show()
                    );
                }
            });
        });
    }
}

