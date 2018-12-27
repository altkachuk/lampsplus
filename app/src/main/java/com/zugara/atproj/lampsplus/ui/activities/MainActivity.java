package com.zugara.atproj.lampsplus.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.ui.activities.base.BaseActivity;
import com.zugara.atproj.lampsplus.ui.fragments.CanvasFragment;
import com.zugara.atproj.lampsplus.ui.fragments.CreateSessionFragment;
import com.zugara.atproj.lampsplus.ui.fragments.LampsFragment;
import com.zugara.atproj.lampsplus.utils.IntentUtils;
import com.zugara.atproj.lampsplus.utils.Requests;

public class MainActivity extends BaseActivity {

    private CreateSessionFragment createSessionFragment;
    private CanvasFragment canvasFragment;
    private LampsFragment lampsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createSessionFragment = (CreateSessionFragment) getFragmentManager().findFragmentById(R.id.createSessionFragment);
        canvasFragment = (CanvasFragment) getFragmentManager().findFragmentById(R.id.canvasFragment);
        lampsFragment = (LampsFragment) getFragmentManager().findFragmentById(R.id.lampsFragment);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == Requests.EXTERNAL_STORAGE_REQUEST) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int res = grantResults[i];
                if (permission.equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) && res == PackageManager.PERMISSION_GRANTED) {
                    if (checkStoragePermissions())
                        IntentUtils.openStorageIntent(this, "image/*", Requests.EXTERNAL_STORAGE_REQUEST);
                }
            }
        }
    }

    private boolean checkStoragePermissions() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == Requests.EXTERNAL_STORAGE_REQUEST) {
            canvasFragment.updateBackground(data);
        }
    }

    public void showCreateSessionFragment() {
        createSessionFragment.getView().setVisibility(View.VISIBLE);
    }

    public void showLampsFragment() {
        if (lampsFragment != null)
            lampsFragment.getView().setVisibility(View.VISIBLE);
    }

    public void hideLampsFragment() {
        if (lampsFragment != null)
            lampsFragment.getView().setVisibility(View.GONE);
    }
}
