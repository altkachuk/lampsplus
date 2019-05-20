package com.atproj.zugara.lampsplus.model;

public class Source {

    public static final String FILE = "file";
    public static final String URL = "url";

    private String source;
    private String source_type;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceType() {
        return source_type;
    }

    public void setSourceType(String sourceType) {
        this.source_type = sourceType;
    }
}
