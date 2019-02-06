package com.zugara.atproj.lampsplus.fileloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by andre on 30-Jan-19.
 */

public class GlowConverter {

    private HashMap<String, File> glowSources;

    public GlowConverter() {
        ;
    }

    public void setGlowSources(HashMap<String, File> glowSources) {
        this.glowSources = glowSources;
    }

    public void convert() {
        for (String key : glowSources.keySet()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(glowSources.get(key).getAbsolutePath(), options);

            Bitmap filteredBitmap = filter(bitmap);
            saveFile(key, filteredBitmap);
        }
    }

    private Bitmap filter(Bitmap bitmap) {
        int pixels[] = new int[bitmap.getWidth()*bitmap.getHeight()];
        int dstPixels[] = new int[bitmap.getWidth()*bitmap.getHeight()];

        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int alpha = Color.red(pixel) - 8;
            if (alpha < 0) alpha = 0;
            int newColor = Color.argb(alpha, 255, 255, 255);
            dstPixels[i] = newColor;
        }

        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        resultBitmap.setPixels(dstPixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        return resultBitmap;
    }

    private void saveFile(String name, Bitmap bitmap) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        path += "/" + name + ".png";

        saveImage(path, bitmap);
    }

    public static boolean saveImage(String path, Bitmap bitmap) {
        File file = new File(path);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
