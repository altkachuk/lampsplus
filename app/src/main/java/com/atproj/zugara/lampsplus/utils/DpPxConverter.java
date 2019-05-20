package com.atproj.zugara.lampsplus.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Andrey on 21.03.2017.
 */
public class DpPxConverter {

    static public int dpToPixel(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int px = (int)(dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    static public float pixelToDp(Context context, int px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dp = (float)px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
