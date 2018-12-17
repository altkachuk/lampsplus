package com.zugara.atproj.lampsplus.ui.activities;

import android.os.Bundle;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.ui.view.DraggableImage;
import com.zugara.atproj.lampsplus.ui.activities.base.BaseActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.imageView)
    DraggableImage imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
