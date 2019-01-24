package com.zugara.atproj.lampsplus.model;

/**
 * Created by andre on 26-Dec-18.
 */

public class Lamp extends BaseFile {

    String id;
    String description;
    Float price;
    BaseFile glow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public BaseFile getGlow() {
        return glow;
    }

    public void setGlow(BaseFile glow) {
        this.glow = glow;
    }
}
