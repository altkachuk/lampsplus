package com.atproj.zugara.lampsplus.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.View;

import com.atproj.zugara.lampsplus.selection.Selectable;

public class SelectImageView extends AppCompatImageView implements Selectable, View.OnTouchListener {

    //private SelectorManager selectorManager;
    protected Bitmap bitmap;
    private boolean selected = false;
    private float scale = 1.0f;
    private OnSelectTouchListener listener;

    public SelectImageView(Context context, Bitmap bitmap) {
        super(context);
        this.bitmap = bitmap;
        setImageBitmap(bitmap);
    }

    @Override
    public boolean isSelect() {
        return selected;
    }

    /*public void setSelectorManager(SelectorManager selectorManager) {
        this.selectorManager = selectorManager;
        selectorManager.addItem(this);
    }*/

    public void setOnSelectTouchListener(OnSelectTouchListener listener) {
        this.listener = listener;
    }

    @Override
    public void select() {
        selected = true;
        bringToFront();
        if (bitmap == null)
            bitmap = ((BitmapDrawable) getDrawable()).getBitmap();

        Bitmap highlightedBitmap = highlightImage();
        setImageBitmap(highlightedBitmap);
    }

    @Override
    public void deselect() {
        selected = false;
        if (bitmap != null) {
            setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean checkPoint(float x, float y) {
        Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        float[] realPoints = new float[]{x, y};
        float[] localPoints = new float[2];
        Matrix invertMatrix = new Matrix();
        getImageMatrix().invert(invertMatrix);
        invertMatrix.mapPoints(localPoints, realPoints);
        if (localPoints[0] <= bitmap.getWidth() && localPoints[1] <= bitmap.getHeight() &&
                localPoints[0] > 0 && localPoints[1] > 0) {
            int pixel = bitmap.getPixel((int) localPoints[0], (int) localPoints[1]);
            if (checkPixel(pixel)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //selectorManager.onTouch(event);
        listener.onTouch(event);
        return true;
    }

    private boolean checkPixel(int pixel) {
        int alphaValue = Color.alpha(pixel);
        return alphaValue > 0;
    }

    public Bitmap highlightImage() {
        // create new bitmap, which will be painted and becomes result image
        Bitmap bmOut = Bitmap.createBitmap(bitmap.getWidth() + 96, bitmap.getHeight() + 96, Bitmap.Config.ARGB_8888);
        // setup canvas for painting
        Canvas canvas = new Canvas(bmOut);
        // setup default color
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        // create a blur paint for capturing alpha
        Paint ptBlur = new Paint();
        ptBlur.setMaskFilter(new BlurMaskFilter(15/scale, BlurMaskFilter.Blur.NORMAL));
        int[] offsetXY = new int[2];
        // capture alpha into a bitmap
        Bitmap bmAlpha = bitmap.extractAlpha(ptBlur, offsetXY);
        // create a color paint
        Paint ptAlphaColor = new Paint();
        ptAlphaColor.setColor(0xFFFF0000);
        // paint color for captured alpha region (bitmap)
        canvas.drawBitmap(bmAlpha, offsetXY[0], offsetXY[1], ptAlphaColor);
        // free memory
        bmAlpha.recycle();

        // paint the image source
        canvas.drawBitmap(bitmap, 0, 0, null);

        // return out final image
        return bmOut;
    }

    public interface OnSelectTouchListener {
        boolean onTouch(MotionEvent event);
    }
}
