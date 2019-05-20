package com.atproj.zugara.lampsplus.views;

import android.graphics.Bitmap;

/**
 * Created by andre on 25-Jan-19.
 */

public interface InvoiceView extends LoadingView {
    void setInvoiceBitmap(Bitmap bitmap);
    void show();
    void hide();
    void clear();
}
