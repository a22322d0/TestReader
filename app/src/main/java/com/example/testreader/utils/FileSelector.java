package com.example.testreader.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FileSelector {

    public interface OnFileSelectedListener {
        void onFileSelected(Uri fileUri, String fileType);
    }

    public static final int REQUEST_CODE_PDF = 1;
    public static final int REQUEST_CODE_EPUB = 2;

    private OnFileSelectedListener listener;

    public FileSelector(OnFileSelectedListener listener) {
        this.listener = listener;
    }

    public void selectPdfFile(Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        fragment.startActivityForResult(intent, REQUEST_CODE_PDF);
    }

    public void selectEpubFile(Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/epub+zip");
        fragment.startActivityForResult(intent, REQUEST_CODE_EPUB);
    }

    public void handleActivityResult(int requestCode, int resultCode, @Nullable Intent data, Context context) {
        if (resultCode == Activity.RESULT_OK && data != null && listener != null) {
            Uri fileUri = data.getData();
            if (requestCode == REQUEST_CODE_PDF) {
                listener.onFileSelected(fileUri, "pdf");
            } else if (requestCode == REQUEST_CODE_EPUB) {
                listener.onFileSelected(fileUri, "epub");
            }
        }
    }
}
