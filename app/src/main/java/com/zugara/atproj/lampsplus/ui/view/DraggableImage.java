package com.zugara.atproj.lampsplus.ui.view;

import android.content.Context;
import android.graphics.Matrix;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zugara.atproj.lampsplus.drag.DragManager;
import com.zugara.atproj.lampsplus.drag.IDraggable;

/**
 * Created by andre on 07-Dec-18.
 */

public class DraggableImage extends AppCompatImageView implements View.OnTouchListener, IDraggable {

    private DragManager dragManager;

    public DraggableImage(Context context) {
        super(context);
        dragManager = new DragManager();
        setOnTouchListener(this);
    }

    public DraggableImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        dragManager = new DragManager();
        setOnTouchListener(this);
    }

    public DraggableImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dragManager = new DragManager();
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        dragManager.onTouch(this, event);
        return true;
    }

    @Override
    public void setMatrix(Matrix matrix) {
        setImageMatrix(matrix);
    }
}
