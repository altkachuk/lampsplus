package com.zugara.atproj.lampsplus.model;

/**
 * Created by andre on 26-Dec-18.
 */

public class Lamp {

    String id;
    String description;
    Float price;

    public Lamp(String id, String description, float price) {
        this.id = id;
        this.description = description;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String name) {
        this.description = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
