package com.zugara.atproj.lampsplus.model.singleton;

import android.graphics.Bitmap;

import com.zugara.atproj.lampsplus.model.InvoiceItem;
import com.zugara.atproj.lampsplus.model.Lamp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 21-Dec-18.
 */

public class SessionContext {

    private String sessionName;
    private String lastImagePath;
    private List<InvoiceItem> invoiceItems = new ArrayList<>();
    private Bitmap designBitmap;
    private Bitmap invoiceBitmap;

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String value) {
        sessionName = value;
    }

    public String getLastImagePath() {
        return lastImagePath;
    }

    public void setLastImagePath(String value) {
        lastImagePath = value;
    }

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void addLampToInvoiceItem(Lamp lamp) {
        boolean exist = false;
        for (InvoiceItem invoiceItem : invoiceItems) {
            if (invoiceItem.getLamp().getId().equals(lamp.getId())) {
                exist = true;
                invoiceItem.setQuantity(invoiceItem.getQuantity() + 1);
                break;
            }
        }
        if (!exist) {
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setLamp(lamp);
            invoiceItem.setQuantity(1);
            invoiceItems.add(invoiceItem);
        }
    }

    public void removeLampFromInvoiceItem(Lamp lamp) {
        for (InvoiceItem invoiceItem : invoiceItems) {
            if (invoiceItem.getLamp().getId().equals(lamp.getId())) {
                if (invoiceItem.getQuantity() > 1) {
                    invoiceItem.setQuantity(invoiceItem.getQuantity()-1);
                } else {
                    invoiceItems.remove(invoiceItem);
                }
                break;
            }
        }
    }

    public void clearInvoiceItems() {
        invoiceItems.clear();
    }

    public Bitmap getDesignBitmap() {
        return designBitmap;
    }

    public void setDesignBitmap(Bitmap designBitmap) {
        this.designBitmap = designBitmap;
    }

    public Bitmap getInvoiceBitmap() {
        return invoiceBitmap;
    }

    public void setInvoiceBitmap(Bitmap invoiceBitmap) {
        this.invoiceBitmap = invoiceBitmap;
    }
}
