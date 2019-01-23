package com.zugara.atproj.lampsplus.drag;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by andre on 23-Jan-19.
 */

public class OnDragTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private float xOffset;

    public OnDragTouchListener(Context context, float xOffset){
        this.xOffset = xOffset;
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public OnDragTouchListener(Context context){
        this(context, 0);
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
            if (e1.getX() > xOffset && e2.getX() < xOffset) {
                onInitDrag();
                return true;
            }
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() > xOffset && e2.getX() < xOffset) {
                onInitDrag();
                return true;
            }
            return false;
        }
    }

    public void onInitDrag() {
    }
}
