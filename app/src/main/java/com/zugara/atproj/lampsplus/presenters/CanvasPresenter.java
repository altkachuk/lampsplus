package com.zugara.atproj.lampsplus.presenters;

import android.util.Log;

import com.zugara.atproj.lampsplus.drag.DragManager;
import com.zugara.atproj.lampsplus.drag.IDraggable;
import com.zugara.atproj.lampsplus.model.Lamp;
import com.zugara.atproj.lampsplus.selection.ISelectable;
import com.zugara.atproj.lampsplus.selection.SelectorManager;
import com.zugara.atproj.lampsplus.views.CanvasView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public class CanvasPresenter {

    private final String TAG = "CanvasPresenter";

    private CanvasView canvasView;
    private SelectorManager selectorManager;
    DragManager.DragEventListener dragEventListener;

    public CanvasPresenter(CanvasView canvasView, SelectorManager selectorManager, DragManager.DragEventListener dragEventListener) {
        this.canvasView = canvasView;
        this.selectorManager = selectorManager;
        this.dragEventListener = dragEventListener;
    }

    public void enable() {
        selectorManager.setEnabled(true);
        dragEventListener.setEnabled(true);
    }

    public void disable() {
        selectorManager.setEnabled(false);
        dragEventListener.setEnabled(false);
    }

    public void upload() {
        canvasView.uploadBackground();
    }

    public void mirror() {
        ((ISelectable)selectorManager.getFrontItem()).mirror();
    }

    public void copy() {
        ISelectable item = (ISelectable) ((IDraggable)selectorManager.getFrontItem()).clone();
        canvasView.addLamp(item);
    }

    public void delete() {
        ISelectable item = selectorManager.getFrontItem();
        if (item != null)
            canvasView.deleteLamp(item);
    }

    public void clear() {
        canvasView.clear();
    }

    public void changeNumOfChildren(int count) {
        canvasView.enableLampButtons(count > 0);
    }

}
