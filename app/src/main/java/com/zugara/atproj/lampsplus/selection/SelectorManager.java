package com.zugara.atproj.lampsplus.selection;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 19-Dec-18.
 */

public class SelectorManager {

    private final int NON_CLICK_MODE = 0;
    private final int CLICK_MODE = 1;

    private int mode = CLICK_MODE;
    private long downEventTime = 0;

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

    public ISelectable removeItem(ISelectable item) {
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
                mode = NON_CLICK_MODE;
                break;
            case MotionEvent.ACTION_DOWN:
                downEventTime = event.getEventTime();
                break;
            case MotionEvent.ACTION_UP:
                if (mode == NON_CLICK_MODE) {
                    mode = (event.getEventTime() - downEventTime < 200) ? CLICK_MODE : NON_CLICK_MODE;
                }
                if (mode == CLICK_MODE) {
                    select(event.getX(), event.getY());
                }
                mode = CLICK_MODE;
                break;
            case MotionEvent.ACTION_MOVE:
                mode = NON_CLICK_MODE;
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
