package com.atproj.zugara.lampsplus.ui.fragments.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.atproj.zugara.lampsplus.dagger.App;

import butterknife.ButterKnife;

/**
 * Created by andre on 07-Dec-18.
 */

public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        injectViews(view);
    }

    private void injectDependencies() {
        App.get(getActivity()).inject(this);
    }

    private void injectViews(View view) {
        ButterKnife.bind(this, view);
    }
}
