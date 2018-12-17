package com.zugara.atproj.lampsplus.ui.activities.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zugara.atproj.lampsplus.dagger.App;

import butterknife.ButterKnife;

/**
 * Created by andre on 07-Dec-18.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectDependencies();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        injectViews();
    }

    private void injectDependencies() {
        App.get(this).inject(this);
    }

    private void injectViews() {
        ButterKnife.bind(this);
    }
}
