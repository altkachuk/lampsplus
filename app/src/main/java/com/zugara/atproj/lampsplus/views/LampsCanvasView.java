package com.zugara.atproj.lampsplus.views;

import android.graphics.Bitmap;

import com.zugara.atproj.lampsplus.model.Lamp;

import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public interface LampsCanvasView extends LoadingView {

    void mirrorTopItem();
    void copyTopItem();
    void removeTopItem();
    List<Lamp> getLamps();
    void updateGlows();

    void enable();
    void disable();
    void clear();

}
