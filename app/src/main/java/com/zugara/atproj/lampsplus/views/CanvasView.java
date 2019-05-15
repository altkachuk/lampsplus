package com.zugara.atproj.lampsplus.views;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.zugara.atproj.lampsplus.model.Lamp;

import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public interface CanvasView extends LoadingView {

    void updateShadow(float shadow);
    void uploadBackground();
    Bitmap createScreenshot();
    void hide();
    void show();
    void clear();

}
