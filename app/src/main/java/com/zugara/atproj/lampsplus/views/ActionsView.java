package com.zugara.atproj.lampsplus.views;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by andre on 27-Dec-18.
 */

public interface ActionsView extends LoadingView {

    void enableLampButtons(boolean enable);
    void gotoCanvasState();
    void gotoCompleteState();
    void showCanvasButtons();
    void hideCanvasButtons();
    void showSessionButtons();
    void hideSessionButtons();
    void showCreateSessionFragment();
    void updateShadowButton(float percent);
    void gotoInvoiceState();
    void gotoDesignState();

}
