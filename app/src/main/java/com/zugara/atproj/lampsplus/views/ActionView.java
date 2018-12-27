package com.zugara.atproj.lampsplus.views;

/**
 * Created by andre on 27-Dec-18.
 */

public interface ActionView extends LoadingView {

    void gotoCanvasState();
    void gotoCompleteState();
    void showCanvasButtons();
    void hideCanvasButtons();
    void showSessionButtons();
    void hideSessionButtons();
    void setInvoice(String text);
    void openImage(String path);
    void createEmail(int subjectResId, int messageResId, String attachmentPath);
    void showCreateSessionFragment();

}
