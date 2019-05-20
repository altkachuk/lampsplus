package com.atproj.zugara.lampsplus.model;

import java.util.List;

public class Lamp extends Product {

    private List<Glow> glows;

    public List<Glow> getGlows() {
        return glows;
    }

    public void setGlows(List<Glow> glows) {
        this.glows = glows;
    }
}
