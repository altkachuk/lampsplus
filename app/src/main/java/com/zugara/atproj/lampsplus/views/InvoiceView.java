package com.zugara.atproj.lampsplus.views;

import android.graphics.Bitmap;

import com.zugara.atproj.lampsplus.model.InvoiceItem;

import java.util.List;

/**
 * Created by andre on 25-Jan-19.
 */

public interface InvoiceView extends LoadingView {
    void setDataProvider(List<InvoiceItem> invoiceItems);
    void setTotal(String text);
    void setTax(String text);
    void setOrderTotal(String text);
    Bitmap createScreenshot();
    void show();
    void hide();
    void clear();
}
