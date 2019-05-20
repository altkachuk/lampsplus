package com.atproj.zugara.lampsplus.selection;

/**
 * Created by andre on 19-Dec-18.
 */

public interface Selectable {

    boolean isSelect();
    boolean checkPoint(float x, float y);
    void select();
    void deselect();

}
