package com.atproj.zugara.lampsplus.model;

import java.util.List;

public class VisibleItem extends Item {

    private String description;

    private List<Source> sources;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }
}
