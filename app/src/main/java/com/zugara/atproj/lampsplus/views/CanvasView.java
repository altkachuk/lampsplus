package com.zugara.atproj.lampsplus.views;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;

import com.zugara.atproj.lampsplus.model.Lamp;
import com.zugara.atproj.lampsplus.selection.ISelectable;
import com.zugara.atproj.lampsplus.selection.SelectorManager;

import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public interface CanvasView extends LoadingView {

    void enable();
    void disable();
    SelectorManager getSelectorManager();
    void deleteLamp(ISelectable item);
    void addLamp(ISelectable item);
    void uploadBackground();
    void setShadow(float percent);
    void addGlow(Object key, Object source);
    void removeGlow(Object key);
    void transformGlow(Object key, Matrix matrix);
    void mirrorGlow(String id);
    Bitmap createScreenshot();
    void hide();
    void show();
    void clear();
}
