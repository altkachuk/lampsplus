package com.zugara.atproj.lampsplus.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andre on 25-Dec-18.
 */

public class ImageUtils {

    public static final String TAG = "ImageUtils";

    static public Bitmap bitmapFromIntent(Context context, Intent data, int width, int height) {
        String path = FileUtils.getActualPath(context, data.getData());
        File selectedFile = new File(path);
        File outFile = createTempImageFile(context);
        compressImageFile(
                selectedFile,
                outFile,
                DpPxConverter.dpToPixel(context, width),
                DpPxConverter.dpToPixel(context, height),
                ScalingUtils.ScalingLogic.CROP);
        Bitmap bitmap = BitmapFactory.decodeFile(outFile.getAbsolutePath());
        return bitmap;
    }

    private static File createTempImageFile(Context context){
        File mediaStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_"+ timeStamp + "_";
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", mediaStorageDir);
        } catch (IOException e) {};

        return image;
    }

    private static void compressImageFile(File inFile, File outFile, int outWidth, int outHeight, ScalingUtils.ScalingLogic scalingLogic) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap scaledBitmap = null;

        try {
            Bitmap unscaledBitmap = ScalingUtils.decodeFile(inFile.getAbsolutePath(), outWidth, outHeight, scalingLogic);
            scaledBitmap = ScalingUtils.createScaledBitmap(unscaledBitmap, outWidth, outHeight, scalingLogic);
            unscaledBitmap.recycle();
        } catch (Exception e) {
            ;
        }


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outFile);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            ;
        }

        scaledBitmap.recycle();
    }
}
