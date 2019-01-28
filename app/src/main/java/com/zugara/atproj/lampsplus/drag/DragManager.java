package com.zugara.atproj.lampsplus.drag;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by andre on 07-Dec-18.
 */

public class DragManager {
    private final String TAG = "DragManager";

    private final float scale_factor = 1.0f;

    // these matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    // remember some things for zooming
    private PointF startPos;
    private PointF currentPos;
    private float startDist;
    private float startRot;

    private DragMode mode;

    public DragManager() {
        startPos = new PointF();
        currentPos = new PointF();
        startDist = 0f;
        startRot = 0f;
        mode = DragMode.NONE;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix.set(matrix);
        savedMatrix.set(matrix);
    }

    /*public void onTouch(IDraggable view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = DragMode.DRAG;

                savedMatrix.set(matrix);

                TouchUtil.midpoint(startPos, event);
                startDist = TouchUtil.spacing(event);
                startRot = TouchUtil.rotation(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = DragMode.NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DragMode.DRAG) {
                    matrix.set(savedMatrix);
                    TouchUtil.midpoint(currentPos, event);

                    // translate
                    matrix.postTranslate(currentPos.x - startPos.x, currentPos.y - startPos.y);

                    if (startDist > 20f) {
                        // scale
                        float newDist = TouchUtil.spacing(event);
                        float scale = (newDist / startDist - 1f) * scale_factor + 1f;
                        matrix.postScale(scale, scale, currentPos.x, currentPos.y);

                        // rotate
                        float newRot = TouchUtil.rotation(event);
                        float r = newRot - startRot;
                        matrix.postRotate(r, currentPos.x, currentPos.y);
                    }

                }
                break;
        }

        view.setMatrix(matrix);
    }*/

    public void onTouch(IDraggable view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = DragMode.DRAG;
                savedMatrix.set(matrix);
                startPos.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = DragMode.ZOOM;
                savedMatrix.set(matrix);
                startDist = spacing(event);
                startRot = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = DragMode.NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DragMode.DRAG) {
                    matrix.set(savedMatrix);
                    // translate
                    float dx = event.getX() - startPos.x;
                    float dy = event.getY() - startPos.y;
                    matrix.postTranslate(dx, dy);
                } else if (mode == DragMode.ZOOM) {
                    float[] src = new float[]{view.getSrcWidth()/2, view.getSrcHeight()/2};
                    float[] dst = new float[2];
                    matrix.mapPoints(dst, src);
                    float px = dst[0];
                    float py = dst[1];

                    // scale
                    float newDist = spacing(event);
                    float scale = (newDist / startDist - 1f) * scale_factor + 1f;
                    matrix.postScale(scale, scale, px, py);
                    startDist = newDist;

                    // rotate
                    float newRot = rotation(event);
                    float r = newRot - startRot;
                    matrix.postRotate(r, px, py);
                    startRot = newRot;
                }
                break;
        }

        view.setMatrix(matrix);
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        float s=x * x + y * y;
        return (float)Math.sqrt(s);
    }

    /**
     * Calculate the mid point of two fingers
     */
    public static void midpoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public static class DragEventListener implements View.OnDragListener {

        private boolean enabled = true;

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        // This is the method that the system calls when it dispatches a drag event to the
        // listener.
        public boolean onDrag(View v, DragEvent event) {
            if (!enabled) return false;

            // Defines a variable to store the action type for the incoming event
            final int action = event.getAction();

            // Handles each of the expected events
            switch(action) {

                case DragEvent.ACTION_DRAG_STARTED:

                    // Determines if this View can accept the dragged data
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        return true;
                    }
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    return true;

                case DragEvent.ACTION_DROP:
                    // Gets the item containing the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    IDraggable view = ((IDraggable) event.getLocalState()).clone();
                    ViewGroup container = (ViewGroup) v;
                    container.addView((View)view);
                    view.move(event.getX(), event.getY());
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    return true;

                // An unknown action type was received.
                default:
                    Log.d("DragDrop Example","Unknown action type received by OnTransformListener.");
                    break;
            }

            return false;
        }
    };
}
