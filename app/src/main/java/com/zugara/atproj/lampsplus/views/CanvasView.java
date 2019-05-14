package com.zugara.atproj.lampsplus.views;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.zugara.atproj.lampsplus.model.Lamp;
import com.zugara.atproj.lampsplus.selection.Selectable;
import com.zugara.atproj.lampsplus.selection.SelectorManager;

import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public interface CanvasView extends LoadingView {

    void enable();
    void disable();
    SelectorManager getSelectorManager();
    void deleteLamp(Selectable item);
    void addLamp(Selectable item);
    void uploadBackground();
    void getLampData(List<Lamp> lamps, List<Matrix> matrices, List<Boolean> mirroredList, List<Integer> sourceWidthList);
    void setGlowBitmap(Bitmap bitmap);
    Bitmap getGlowSourceBitmap();
    int getWidth();
    int getHeight();
    void setGlows(List<Object> sources, List<Matrix> matrices);
    void clearGlow();
    Bitmap createScreenshot();
    void hide();
    void show();
    void clear();
}
