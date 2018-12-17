package com.zugara.atproj.lampsplus.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zugara.atproj.lampsplus.ui.activities.base.BaseActivity;
import com.zugara.atproj.lampsplus.utils.ApplicationUtil;

/**
 * Created by andre on 15-Dec-18.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationUtil.startActivity(this, MainActivity.class);
    }
}
