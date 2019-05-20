package com.atproj.zugara.lampsplus.views;

import android.graphics.Bitmap;

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
