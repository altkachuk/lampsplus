package com.zugara.atproj.lampsplus.views;

/**
 * Created by andre on 15-Dec-18.
 */

public interface LoadingView {
    void showPreloader();
    void hidePreloader();
    void showErrorMessage(String message);
    void showErrorMessage(int messageResId);
}
