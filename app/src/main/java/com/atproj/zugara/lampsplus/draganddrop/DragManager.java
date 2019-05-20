package com.atproj.zugara.lampsplus.draganddrop;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by andre on 07-Dec-18.
 */

public class DragManager {
    private final String TAG = "DragManager";

    private final float scale_factor = 1.0f;

    private Rect bounddary = new Rect(0, 0, 600, 600);

    // these matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private Matrix savedDragMatrix = new Matrix();

    // remember some things for zooming
    private PointF startPos;
    private float startDist;
    private float startRot;

    private DragMode mode;

    public DragManager() {
        startPos = new PointF();
        startDist = 0f;
        startRot = 0f;
        mode = DragMode.NONE;
    }

    public void setBound(Rect value) {
        bounddary = value;
    }

    public void setBound(int left, int top, int right, int bottom) {
        bounddary = new Rect(left, top, right, bottom);
    }

    public void setMatrix(Matrix matrix) {
        this.matrix.set(matrix);
        savedMatrix.set(matrix);
    }

    public void onTouch(ImageView view, int width, int height, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = DragMode.DRAG;
                savedMatrix.set(matrix);
                // init start data
                startPos.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = DragMode.ZOOM_AND_ROTATE;
                savedMatrix.set(matrix);
                // init start data
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
                    float[] values = new float[9];
                    matrix.getValues(values);
                    float x = values[Matrix.MTRANS_X];
                    float y = values[Matrix.MTRANS_Y];
                    float dx = event.getX() - startPos.x;
                    float dy = event.getY() - startPos.y;

                    if (x + dx > bounddary.left && x + dx < bounddary.right
                            &&  y + dy > bounddary.top && y + dy < bounddary.bottom) {
                        // translate
                        matrix.postTranslate(dx, dy);
                        savedDragMatrix.set(matrix);
                    } else {
                        savedMatrix.set(savedDragMatrix);
                        matrix.set(savedDragMatrix);
                        startPos.set(event.getX(), event.getY());
                    }
                } else if (mode == DragMode.ZOOM_AND_ROTATE) {
                    float[] src = new float[]{width / 2, height / 2};
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

        view.setImageMatrix(matrix);
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        float s = x * x + y * y;
        return (float) Math.sqrt(s);
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
}
