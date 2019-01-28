package com.zugara.atproj.lampsplus.drag;

import android.graphics.Matrix;

/**
 * Created by andre on 25-Jan-19.
 */

public interface OnTransformListener {
    void onMatrixChange(Matrix matrix);
    void onMirror();
}
