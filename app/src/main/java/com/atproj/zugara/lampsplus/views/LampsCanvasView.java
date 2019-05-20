package com.atproj.zugara.lampsplus.views;

import com.atproj.zugara.lampsplus.model.Lamp;

import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public interface LampsCanvasView extends LoadingView {

    void mirrorTopItem();
    void copyTopItem();
    void removeTopItem();
    List<Lamp> getLamps();
    void updateGlows();

    void enable();
    void disable();
    void clear();

}
