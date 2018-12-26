package com.zugara.atproj.lampsplus.model;

/**
 * Created by andre on 17-Dec-18.
 */

public class ItemFile {

    private String name;
    private Object source;
    private Lamp lamp;

    public ItemFile(String name, Object source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public Object getSource() {
        return source;
    }

    public Lamp getLamp() {
        return lamp;
    }

    public void setLamp(Lamp lamp) {
        this.lamp = lamp;
    }
}
