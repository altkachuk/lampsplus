package com.zugara.atproj.lampsplus.views;

import com.zugara.atproj.lampsplus.selection.ISelectable;

/**
 * Created by andre on 15-Dec-18.
 */

public interface CanvasView extends LoadingView {

    void enableLampButtons(boolean enable);
    void deleteLamp(ISelectable item);
    void addLamp(ISelectable item);
    void uploadBackground();
    void setShadow(float percent);
    void clear();
}
