package com.zugara.atproj.lampsplus.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.zugara.atproj.lampsplus.draganddrop.DragManager;
import com.zugara.atproj.lampsplus.draganddrop.Draggable;

import java.lang.reflect.Field;

public class LampImageView extends MirrorImageView implements View.OnTouchListener, Draggable {

    private DragManager dragManager;
    private boolean touchEnabled = true;

    public LampImageView(Context context, Bitmap bitmap) {
        super(context, bitmap);

        setScaleType(ScaleType.MATRIX);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dragManager = new DragManager();
        setOnTouchListener(this);
    }

    @Override
    public void setBound(Rect value) {
        dragManager.setBound(value);
    }

    @Override
    public void setBound(int left, int top, int right, int bottom) {
        Rect boundary = new Rect(left, top, right, bottom);
        dragManager.setBound(boundary);
    }

    @Override
    public void move(float x, float y) {
        Matrix matrix = getImageMatrix();
        Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        float localX = x - bitmap.getWidth() / 2.0f;
        float localY = y - bitmap.getHeight() / 2.0f;
        matrix.postTranslate(localX, localY);
        setImageMatrix(matrix);
        dragManager.setMatrix(matrix);
    }

    @Override
    public void enableTouch() {
        touchEnabled = true;
    }

    @Override
    public void disableTouch() {
        touchEnabled = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!touchEnabled) {
            return false;
        }

        dragManager.onTouch(this, event);

        return super.onTouch(v, event);
    }

    @Override
    public Draggable clone() {
        Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        LampImageView lampView = new LampImageView(getContext(), bitmap);
        lampView.setImageBitmap(getBitmap());
        lampView.setTag(this.getTag());
        if (isMirrored()) {
            try {
                Field isMirroredField = lampView.getClass().getDeclaredField("isMirrored");
                isMirroredField.setAccessible(true);
                isMirroredField.set(lampView, isMirrored());
            } catch (Exception e) {};
        }
        return lampView;
    }
}
