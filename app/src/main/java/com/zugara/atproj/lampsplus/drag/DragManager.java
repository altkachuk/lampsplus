package com.zugara.atproj.lampsplus.drag;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by andre on 07-Dec-18.
 */

public class DragManager {

    private final float scale_factor = 1.5f;

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

                    // scale
                    float newDist = TouchUtil.spacing(event);
                    if (newDist > 10f) {
                        float scale = newDist / startDist * scale_factor;
                        matrix.postScale(scale, scale, currentPos.x, currentPos.y);
                    }

                    // rotate
                    float newRot = TouchUtil.rotation(event);
                    float r = newRot - startRot;
                    matrix.postRotate(r, currentPos.x, currentPos.y);

                }
                break;
        }

        view.setMatrix(matrix);
    }
}
