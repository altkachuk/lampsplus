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
import android.widget.Toast;

import com.zugara.atproj.lampsplus.ui.view.DraggableImage;

/**
 * Created by andre on 07-Dec-18.
 */

public class DragManager {

    private final float scale_factor = 0.5f;

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

    public void onTouch(IDraggable view, MotionEvent event) {
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

                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    return true;

                // An unknown action type was received.
                default:
                    Log.d("DragDrop Example","Unknown action type received by OnDragListener.");
                    break;
            }

            return false;
        }
    };
}
