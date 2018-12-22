package com.zugara.atproj.lampsplus.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Andrey on 21.03.2017.
 */
public class MesuarementUtil {

    static public int dpToPixel(Context context, float dp){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int px = (int)(dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
