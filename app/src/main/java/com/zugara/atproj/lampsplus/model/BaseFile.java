package com.zugara.atproj.lampsplus.model;

/**
 * Created by andre on 24-Jan-19.
 */

public class BaseFile {
    String name;
    Object source;
    BaseFile parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public BaseFile getParent() {
        return parent;
    }

    public void setParent(BaseFile parent) {
        this.parent = parent;
    }
}
