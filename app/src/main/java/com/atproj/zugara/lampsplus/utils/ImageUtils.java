package com.atproj.zugara.lampsplus.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

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
        Uri contentURI = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
            return compressBitmap(bitmap, width, height, ScalingUtils.ScalingLogic.CROP);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap compressBitmap(Bitmap srcBitmap, int outWidth, int outHeight, ScalingUtils.ScalingLogic scalingLogic) {
        Bitmap outBitmap = ScalingUtils.createScaledBitmap(srcBitmap, outWidth, outHeight, scalingLogic);
        return outBitmap;
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
