package com.zugara.atproj.lampsplus.draganddrop;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by andre on 23-Jan-19.
 */

public class OnDragTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;

    public OnDragTouchListener(Context context){
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // left motion
            if (e2.getX() - e1.getX() < 0) {
                // left swipe
                if (distanceX  >= distanceY) {
                    onInitDrag();
                    return true;
                }
            }

            return false;
        }
    }

    public void onInitDrag() {
    }
}
