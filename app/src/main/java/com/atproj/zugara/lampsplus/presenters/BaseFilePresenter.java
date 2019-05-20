package com.atproj.zugara.lampsplus.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.atproj.zugara.lampsplus.model.singleton.SessionContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by andre on 04-Feb-19.
 */

public class BaseFilePresenter extends BasePresenter {

    public static final String TAG = "BaseFilePresenter";

    @Inject
    SessionContext sessionContext;

    public BaseFilePresenter(Context context) {
        super(context);
    }

    public String downloadImage(final Bitmap srcBitmap, String prefix) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        path += "/" + sessionContext.getSessionName() + "_" + prefix;
        path += "_" + sessionContext.getTimestamp();
        path += ".jpg";

        final String fPath = path;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(srcBitmap);
                saveImage(fPath, bitmap);
            }
        }).start();
        return path;
    }

    private boolean saveImage(String path, Bitmap bitmap) {
        File file = new File(path);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.getMessage());
            return false;
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            return false;
        }

        return true;
    }
}
