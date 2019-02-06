package com.zugara.atproj.lampsplus.model.singleton;

import android.graphics.Bitmap;

import com.zugara.atproj.lampsplus.model.InvoiceItem;
import com.zugara.atproj.lampsplus.model.Lamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by andre on 21-Dec-18.
 */

public class SessionContext {

    private String sessionName;
    private String timestamp;
    private String lastImagePath;
    private List<InvoiceItem> invoiceItems = new ArrayList<>();
    private String designPath;
    private String invoicePath;

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String value) {
        sessionName = value;
        timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public String getTimestamp() {
        return timestamp;
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

    public void setInvoiceItemsProvider(List<Lamp> lamps) {
        invoiceItems.clear();
        for (Lamp lamp : lamps) {
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
    }

    public void clearInvoiceItems() {
        invoiceItems.clear();
    }

    public String getDesignPath() {
        return designPath;
    }

    public void setDesignPath(String designPath) {
        this.designPath = designPath;
    }

    public String getInvoicePath() {
        return invoicePath;
    }

    public void setInvoicePath(String invoicePath) {
        this.invoicePath = invoicePath;
    }
}
