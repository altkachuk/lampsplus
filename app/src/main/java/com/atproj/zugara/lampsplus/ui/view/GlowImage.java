package com.atproj.zugara.lampsplus.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by andre on 25-Jan-19.
 */

public class GlowImage extends AppCompatImageView {

    private Bitmap oldBitmap;

    public GlowImage(Context context) {
        super(context);
        setScaleType(ScaleType.MATRIX);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public GlowImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
    }

    public GlowImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
    }

    public void mirror() {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);

        if (oldBitmap == null)
            oldBitmap = ((BitmapDrawable) getDrawable()).getBitmap();

        Bitmap outBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, true);
        setImageBitmap(outBitmap);
        oldBitmap = outBitmap;
    }
}
