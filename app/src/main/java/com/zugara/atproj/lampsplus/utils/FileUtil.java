package com.zugara.atproj.lampsplus.utils;

import android.os.Environment;

import com.zugara.atproj.lampsplus.BuildConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public class FileUtil {

    public static List<File> getListFiles(File parentDir) {
        List<File> result = new ArrayList<>();

        File[] files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                result.add(file);
            }
        }

        return result;
    }

    public static File getAppDirectory() {
        return new File(Environment.getExternalStorageDirectory() + "/" + BuildConfig.FLAVOR);
    }
}
