package com.zugara.atproj.lampsplus.drag;

import android.graphics.Matrix;
import android.graphics.Rect;

/**
 * Created by andre on 07-Dec-18.
 */

public interface IDraggable {
    float getSrcWidth();
    float getSrcHeight();
    void setBound(Rect value);
    void setBound(int left, int top, int right, int bottom);
    void setMatrix(Matrix matrix);
    void move(float x, float y);
    void enableTouch();
    void disableTouch();
    IDraggable clone();
}
