// FileUtil.java
/*
* 新建一个文件并解压。这段本来是在Fragment中的，为了简化Fragment
* 于是单独抽离出来。
* */

package com.example.testreader.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.List;

public class FileUtil {

    public static List<File> extractImagesFromCBZ(Uri uri, Context context) {
        File destinationDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "CBZImages");
        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }
        // 使用 CbzExtractor 解压文件
        return CbzExtractor.extract(uri, destinationDir, context);
    }
}

