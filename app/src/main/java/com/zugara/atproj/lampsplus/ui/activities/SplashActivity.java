package com.zugara.atproj.lampsplus.ui.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.zugara.atproj.lampsplus.ui.activities.base.BaseActivity;
import com.zugara.atproj.lampsplus.utils.ApplicationUtil;
import com.zugara.atproj.lampsplus.utils.IntentUtils;
import com.zugara.atproj.lampsplus.utils.Requests;

/**
 * Created by andre on 15-Dec-18.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkStoragePermissions()) {
            ApplicationUtil.startActivity(this, MainActivity.class);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    Requests.EXTERNAL_STORAGE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == Requests.EXTERNAL_STORAGE_REQUEST) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int res = grantResults[i];
                if (permission.equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) && res == PackageManager.PERMISSION_GRANTED) {
                    if (checkStoragePermissions()) {
                        ApplicationUtil.startActivity(this, MainActivity.class);
                        return;
                    }
                }
            }
        }

        ActivityCompat.requestPermissions(this,
                new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                Requests.EXTERNAL_STORAGE_REQUEST);
    }

    private boolean checkStoragePermissions() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}
