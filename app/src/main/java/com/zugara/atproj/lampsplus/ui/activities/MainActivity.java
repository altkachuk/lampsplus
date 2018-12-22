package com.zugara.atproj.lampsplus.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.ui.activities.base.BaseActivity;
import com.zugara.atproj.lampsplus.ui.fragments.CanvasFragment;

public class MainActivity extends BaseActivity {

    private CanvasFragment canvasFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        canvasFragment = (CanvasFragment) getFragmentManager().findFragmentById(R.id.canvasFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == CanvasFragment.STORAGE_REQUEST) {
            canvasFragment.updateBackground(data);
        }
    }
}
