package com.zugara.atproj.lampsplus.model;

/**
 * Created by andre on 25-Jan-19.
 */

public class InvoiceItem {

    Lamp lamp;
    int quantity;

    public Lamp getLamp() {
        return lamp;
    }

    public void setLamp(Lamp lamp) {
        this.lamp = lamp;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalPrice() {
        float totalPrice = (float)quantity * lamp.getPrice();
        return totalPrice;
    }
}
