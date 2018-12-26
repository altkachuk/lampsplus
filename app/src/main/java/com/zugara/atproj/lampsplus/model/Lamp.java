package com.zugara.atproj.lampsplus.model;

/**
 * Created by andre on 26-Dec-18.
 */

public class Lamp {

    String id;
    String name;
    Float price;

    public Lamp(String id, float price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
