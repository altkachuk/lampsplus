package com.zugara.atproj.lampsplus.draganddrop;

import android.graphics.Rect;

/**
 * Created by andre on 07-Dec-18.
 */

public interface Draggable {
    void setBound(Rect value);
    void setBound(int left, int top, int right, int bottom);
    void move(float x, float y);
    Draggable clone();
}
