package com.atproj.zugara.lampsplus.ui.view;

/**
 * Created by andre on 07-Dec-18.
 */

public class DraggableImage /*extends AppCompatImageView implements View.OnTouchListener, Draggable, Selectable*/ {

    /*private final String TAG = "DraggableImage";

    private DragManager dragManager;
    private SelectorManager selectorManager;
    private Bitmap oldBitmap;
    private boolean selected = false;
    private boolean isMirrored = false;
    private boolean touchEnabled = true;

    public DraggableImage(Context context) {
        super(context);
        setScaleType(ScaleType.MATRIX);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dragManager = new DragManager();
        setOnTouchListener(this);
    }

    public DraggableImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
        dragManager = new DragManager();
        setOnTouchListener(this);
    }

    public DraggableImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
        dragManager = new DragManager();
        setOnTouchListener(this);
    }

    public void setSelectorManager(SelectorManager selectorManager) {
        this.selectorManager = selectorManager;
        selectorManager.addItem(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (!touchEnabled) return false;

        dragManager.onTouch(this, event);
        selectorManager.onTouch(this, event);
        return true;
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
    public void setImageMatrix(Matrix matrix) {
        super.setImageMatrix(matrix);

        if (selected) {
            Bitmap highlightedBitmap = highlightImage(oldBitmap, getCurrentScale());
            setImageBitmap(highlightedBitmap);
        }
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
    public void mirror() {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        Bitmap outBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, true);
        setImageBitmap(outBitmap);
        oldBitmap = outBitmap;

        if (selected) {
            Bitmap highlightedBitmap = highlightImage(oldBitmap, getCurrentScale());
            setImageBitmap(highlightedBitmap);
        }

        isMirrored = !isMirrored;
    }

    public boolean isMirrored() {
        return isMirrored;
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

    private boolean checkPixel(int pixel) {
        int alphaValue = Color.alpha(pixel);
        return alphaValue > 0;
    }

    @Override
    public void select() {
        selected = true;
        bringToFront();
        if (oldBitmap == null)
            oldBitmap = ((BitmapDrawable) getDrawable()).getBitmap();

        Bitmap highlightedBitmap = highlightImage(oldBitmap, getCurrentScale());
        setImageBitmap(highlightedBitmap);
    }

    private float getCurrentScale() {
        float[] values = new float[9];
        Matrix matrix = getImageMatrix();
        matrix.getValues(values);
        // calculate real scale
        float scaleX = values[Matrix.MSCALE_X];
        float skewY = values[Matrix.MSKEW_Y];
        float realScale = (float) Math.sqrt(scaleX * scaleX + skewY * skewY);
        return realScale;
    }

    public void deselect() {
        selected = false;
        if (oldBitmap != null) {
            setImageBitmap(oldBitmap);
        }
    }

    public Bitmap highlightImage(Bitmap src, float scale) {
        // create new bitmap, which will be painted and becomes result image
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth() + 96, src.getHeight() + 96, Bitmap.Config.ARGB_8888);
        // setup canvas for painting
        Canvas canvas = new Canvas(bmOut);
        // setup default color
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        // create a blur paint for capturing alpha
        Paint ptBlur = new Paint();
        ptBlur.setMaskFilter(new BlurMaskFilter(15/scale, BlurMaskFilter.Blur.NORMAL));
        int[] offsetXY = new int[2];
        // capture alpha into a bitmap
        Bitmap bmAlpha = src.extractAlpha(ptBlur, offsetXY);
        // create a color paint
        Paint ptAlphaColor = new Paint();
        ptAlphaColor.setColor(0xFFFF0000);
        // paint color for captured alpha region (bitmap)
        canvas.drawBitmap(bmAlpha, offsetXY[0], offsetXY[1], ptAlphaColor);
        // free memory
        bmAlpha.recycle();

        // paint the image source
        canvas.drawBitmap(src, 0, 0, null);

        // return out final image
        return bmOut;
    }

    public DraggableImage clone() {
        DraggableImage image = new DraggableImage(getContext());
        if (oldBitmap == null) {
            oldBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        }
        image.setImageBitmap(oldBitmap);
        image.setTag(this.getTag());
        image.isMirrored = isMirrored;
        return image;
    }*/
}
