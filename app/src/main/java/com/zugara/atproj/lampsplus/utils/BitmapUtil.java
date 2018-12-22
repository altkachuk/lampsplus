package com.zugara.atproj.lampsplus.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import java.io.File;

/**
 * Created by andre on 22-Dec-18.
 */

public class BitmapUtil {

    static public Bitmap bitmapFromIntent(Context context, Intent data, int width, int height) {
        String path = FileUtil.getActualPath(context, data.getData());
        File selectedFile = new File(path);
        File outFile = FileUtil.createImageFile(context);
        FileUtil.compressImage(
                selectedFile,
                outFile,
                MesuarementUtil.dpToPixel(context, width),
                MesuarementUtil.dpToPixel(context, height),
                ScalingUtil.ScalingLogic.CROP);
        Bitmap bitmap = BitmapFactory.decodeFile(outFile.getAbsolutePath());
        return bitmap;
    }

    static public Bitmap createScreenshot(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        return bitmap;
    }
}
