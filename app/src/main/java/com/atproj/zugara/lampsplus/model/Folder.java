package com.atproj.zugara.lampsplus.model;

import java.util.ArrayList;
import java.util.List;

public class Folder extends Item {

    private List<Item> children;

    public Folder() {
        children = new ArrayList<>();
    }

    public List<Item> getChildren() {
        return children;
    }

    public void addChild(Item child) {
        children.add(child);
        children.get(children.size()-1).setParent(this);
    }

    public Item getChildAt(int index) {
        if (children != null && children.size() > index) {
            return children.get(index);
        }
        return null;
    }

}
