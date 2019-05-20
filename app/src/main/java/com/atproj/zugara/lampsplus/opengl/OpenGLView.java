package com.atproj.zugara.lampsplus.opengl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by andre on 29-Jan-19.
 */

public class OpenGLView extends GLSurfaceView {

    public OpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat( PixelFormat.TRANSLUCENT );
        setRenderer(new OpenGLRenderer(context));
    }
}
