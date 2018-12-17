package com.zugara.atproj.lampsplus.model;

/**
 * Created by andre on 17-Dec-18.
 */

public class FileItem {

    private String name;
    private Object source;

    public FileItem(String name, Object source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public Object getSource() {
        return source;
    }
}
