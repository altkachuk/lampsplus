package com.atproj.zugara.lampsplus.views;

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
