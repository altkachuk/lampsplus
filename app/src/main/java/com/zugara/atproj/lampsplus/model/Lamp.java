package com.zugara.atproj.lampsplus.model;

import android.graphics.Matrix;
import android.graphics.Point;

/**
 * Created by andre on 26-Dec-18.
 */

public class Lamp extends BaseFile {

    String id;
    String light_id;
    Point point;
    Integer rotation;
    Float scale;
    String description;
    Float price;
    BaseFile glow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLightId() {
        return light_id;
    }

    public void setLightId(String lightId) {
        this.light_id = lightId;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    public Float getScale() {
        return scale;
    }

    public void setScale(Float scale) {
        this.scale = scale;
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

    public Matrix getGlowMatrix() {
        Matrix matrix = new Matrix();
        matrix.postTranslate(point.x, point.y);
        return matrix;
    }
}
