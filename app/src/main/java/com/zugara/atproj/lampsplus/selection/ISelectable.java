package com.zugara.atproj.lampsplus.selection;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by andre on 19-Dec-18.
 */

public interface ISelectable {
    void setSelectorManager(SelectorManager selectorManager);
    boolean checkPoint(float x, float y);
    void mirror();
    void select();
    void deselect();
}
