package com.zugara.atproj.lampsplus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 24-Jan-19.
 */

public class Folder extends BaseFile {

    List<BaseFile> children;

    public Folder() {
        children = new ArrayList<>();
    }

    public List<BaseFile> getChildren() {
        return children;
    }

    public void setChildren(List<BaseFile> children) {
        this.children = children;
    }

    public void addChild(BaseFile file) {
        children.add(file);
        children.get(children.size()-1).setParent(this);
    }

    public BaseFile getChild(int index) {
        if (children != null && children.size() > index) {
            return children.get(index);
        }
        return null;
    }
}
