package com.zugara.atproj.lampsplus.selection;

import android.view.MotionEvent;
import android.view.View;

import com.zugara.atproj.lampsplus.drag.DragMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 19-Dec-18.
 */

public class SelectorManager {

    private DragMode mode = DragMode.NONE;

    private List<ISelectable> itemList = new ArrayList<>();
    private boolean enabled = true;

    public SelectorManager() {

    }

    public void addItem(ISelectable item) {
        if (itemList.size() > 0) {
            itemList.get(itemList.size()-1).deselect();
        }
        itemList.add(item);
        item.select();;
    }

    public ISelectable removeFrontItem() {
        ISelectable item = getFrontItem();
        if (item != null) {
            item.deselect();
            itemList.remove(item);
            if (itemList.size() > 0) {
                itemList.get(itemList.size() - 1).select();
            }
        }
        return item;
    }

    public ISelectable getFrontItem() {
        if (itemList.size() > 0)
            return itemList.get(itemList.size()-1);
        return null;
    }



    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            if (itemList.size() > 0) {
                itemList.get(itemList.size() - 1).select();
            }
        } else {
            deselectAll();
        }
    }

    private void deselectAll() {
        for (int i = 0; i < itemList.size(); i++) {
            ISelectable item = itemList.get(i);
            item.deselect();
        }
    }

    public void onTouch(View view, MotionEvent event) {
        if (!enabled) return;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = DragMode.DRAG;
                break;
            case MotionEvent.ACTION_UP:
                if (mode == DragMode.NONE) {
                    select(event.getX(), event.getY());
                }
                mode = DragMode.NONE;
                break;
        }
    }

    private void select(float x, float y) {
        for (int i = 0; i < itemList.size(); i++) {
            ISelectable item = itemList.get(i);
            if (item.checkPoint(x, y)) {
                itemList.remove(item);
                if (itemList.size() > 0) {
                    itemList.get(itemList.size()-1).deselect();
                }
                itemList.add(item);
                item.select();
                break;
            }
        }
    }
}
