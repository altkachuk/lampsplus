package com.zugara.atproj.lampsplus.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by andre on 15-Dec-18.
 */

public class ApplicationUtil {

    public static void startActivity(Activity srcActivity, Class dstActivity) {
        Context context = srcActivity.getApplicationContext();
        Intent intent = new Intent(context, dstActivity);
        srcActivity.startActivity(intent);
        srcActivity.finish();
    }
}
