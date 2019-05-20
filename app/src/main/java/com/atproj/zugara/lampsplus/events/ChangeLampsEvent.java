package com.atproj.zugara.lampsplus.events;

/**
 * Created by andre on 27-Jan-19.
 */

public class ChangeLampsEvent {

    private int count;

    public ChangeLampsEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
