package com.zugara.atproj.lampsplus.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.zugara.atproj.lampsplus.selection.Mirrorable;

public class MirrorImageView extends SelectImageView implements Mirrorable {

    private boolean isMirrored = false;

    public MirrorImageView(Context context, Bitmap bitmap) {
        super(context, bitmap);
    }

    @Override
    public void mirror() {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        Bitmap outBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        setImageBitmap(outBitmap);
        bitmap = outBitmap;

        if (isSelect()) {
            Bitmap highlightedBitmap = highlightImage();
            setImageBitmap(highlightedBitmap);
        }

        isMirrored = !isMirrored;
    }

    @Override
    public boolean isMirrored() {
        return isMirrored;
    }
}
