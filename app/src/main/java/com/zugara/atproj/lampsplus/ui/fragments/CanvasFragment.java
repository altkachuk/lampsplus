package com.zugara.atproj.lampsplus.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.presenters.CanvasPresenter;
import com.zugara.atproj.lampsplus.ui.fragments.base.BaseFragment;
import com.zugara.atproj.lampsplus.views.CanvasView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by andre on 15-Dec-18.
 */

public class CanvasFragment extends BaseFragment implements CanvasView {

    @BindView(R.id.copyButton)
    Button copyButton;

    @BindView(R.id.deleteButton)
    Button deleteButton;

    private CanvasPresenter canvasPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_canvas, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        canvasPresenter = new CanvasPresenter(this);
    }

    @OnClick(R.id.uploadButton)
    public void onClickUploadButton() {
        canvasPresenter.upload();
    }

    @OnClick(R.id.copyButton)
    public void onClickCopyButton() {
        canvasPresenter.copy();
    }

    @OnClick(R.id.deleteButton)
    public void onClickDeleteButton() {
        canvasPresenter.delete();
    }

    public void enableLampButtons() {
        copyButton.setEnabled(true);
        deleteButton.setEnabled(true);
    }

    public void disableLampsButtons() {
        copyButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }
}
