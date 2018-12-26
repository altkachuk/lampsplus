package com.zugara.atproj.lampsplus.drag;

import android.graphics.Matrix;

/**
 * Created by andre on 07-Dec-18.
 */

public interface IDraggable {
    float getSrcWidth();
    float getSrcHeight();
    void setMatrix(Matrix matrix);
    void move(float x, float y);
    IDraggable clone();
}
