package com.zugara.atproj.lampsplus.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.drag.DragManager;
import com.zugara.atproj.lampsplus.model.singleton.SessionContext;
import com.zugara.atproj.lampsplus.presenters.CanvasPresenter;
import com.zugara.atproj.lampsplus.selection.ISelectable;
import com.zugara.atproj.lampsplus.selection.SelectorManager;
import com.zugara.atproj.lampsplus.ui.activities.MainActivity;
import com.zugara.atproj.lampsplus.ui.fragments.base.BaseFragment;
import com.zugara.atproj.lampsplus.utils.ImageUtils;
import com.zugara.atproj.lampsplus.utils.IntentUtils;
import com.zugara.atproj.lampsplus.utils.Requests;
import com.zugara.atproj.lampsplus.views.CanvasView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by andre on 15-Dec-18.
 */

public class CanvasFragment extends BaseFragment implements CanvasView {

    @Inject
    SessionContext sessionContext;

    @BindView(R.id.screenshotHolder)
    RelativeLayout screenshotHolder;

    @BindView(R.id.lampsHolder)
    RelativeLayout lampsHolder;

    @BindView(R.id.canvasButtonHolder)
    LinearLayout canvasButtonHolder;

    @BindView(R.id.sessionButtonHolder)
    LinearLayout sessionButtonHolder;

    @BindView(R.id.backgroundView)
    ImageView backgroundView;

    @BindView(R.id.uploadButton)
    Button uploadButton;

    @BindView(R.id.copyButton)
    Button copyButton;

    @BindView(R.id.deleteButton)
    Button deleteButton;

    @BindView(R.id.completeButton)
    Button completeButton;

    @BindView(R.id.continueButton)
    Button continueButton;

    @BindView(R.id.downloadButton)
    Button downloadButton;

    private ProgressDialog progressDialog;

    private CanvasPresenter canvasPresenter;
    private SelectorManager selectorManager;

    //-------------------------------------------------------------
    // Lifecycle override

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_canvas, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPreloader();

        DragManager.DragEventListener dragEventListener = new DragManager.DragEventListener();
        lampsHolder.setOnDragListener(dragEventListener);
        selectorManager = new SelectorManager();

        lampsHolder.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                ((ISelectable) child).setSelectorManager(selectorManager);
                canvasPresenter.changeNumOfChildren(lampsHolder.getChildCount());
                canvasPresenter.addTag(child.getTag());
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                selectorManager.removeItem((ISelectable) child);
                canvasPresenter.changeNumOfChildren(lampsHolder.getChildCount());
                canvasPresenter.removeTag(child.getTag());
            }
        });

        canvasPresenter = new CanvasPresenter(this, sessionContext, selectorManager, dragEventListener);

        canvasPresenter.changeNumOfChildren(lampsHolder.getChildCount());
    }

    //-------------------------------------------------------------
    // Handlers

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

    @OnClick(R.id.completeButton)
    public void onCompleteButton() {
        canvasPresenter.complete();
    }

    @OnClick(R.id.continueButton)
    public void onClickContinueButton() {
        canvasPresenter.continueAction();
    }

    @OnClick(R.id.downloadButton)
    public void onClickDownloadButton() {
        screenshotHolder.setDrawingCacheEnabled(true);
        canvasPresenter.download(screenshotHolder.getDrawingCache());
    }

    @OnClick(R.id.openButton)
    public void onClickOpenButton() {
        canvasPresenter.open();
    }

    @OnClick(R.id.sendButton)
    public void onClickSendButton() {
        canvasPresenter.send();
    }

    @OnClick(R.id.finishButton)
    public void onClickFinishButton() {
        canvasPresenter.finish();
    }

    @OnClick(R.id.newSessionButton)
    public void onClickNewSessionButton() {
        canvasPresenter.newSession();
    }

    //-------------------------------------------------------------
    // LoadingView methods

    @Override
    public void showPreloader() {
        progressDialog.show();
    }

    @Override
    public void hidePreloader() {
        progressDialog.dismiss();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.name_has_illegal_characters), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(int messageResId) {
        showErrorMessage(getString(messageResId));
    }

    //-------------------------------------------------------------
    // CanvasView methods

    @Override
    public void enableLampButtons(boolean enable) {
        copyButton.setEnabled(enable);
        deleteButton.setEnabled(enable);

        if (enable) {
            copyButton.setAlpha(0.75f);
            deleteButton.setAlpha(0.75f);
        } else {
            copyButton.setAlpha(1f);
            deleteButton.setAlpha(1f);
        }
    }

    @Override
    public void gotoCanvasState() {
        uploadButton.setVisibility(View.VISIBLE);
        copyButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        completeButton.setVisibility(View.VISIBLE);
        continueButton.setVisibility(View.GONE);
        downloadButton.setVisibility(View.GONE);
    }

    @Override
    public void gotoCompleteState() {
        uploadButton.setVisibility(View.GONE);
        copyButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        completeButton.setVisibility(View.GONE);
        continueButton.setVisibility(View.VISIBLE);
        downloadButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCanvasButtons() {
        canvasButtonHolder.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCanvasButtons() {
        canvasButtonHolder.setVisibility(View.GONE);
    }

    @Override
    public void showSessionButtons() {
        sessionButtonHolder.setVisibility(View.VISIBLE);
    }

    public void hideSessionButtons() {
        sessionButtonHolder.setVisibility(View.GONE);
    }

    @Override
    public void deleteLamp(ISelectable item) {
        lampsHolder.removeView((View)item);
    }

    @Override
    public void addLamp(ISelectable item) {
        lampsHolder.addView((View) item);
    }

    @Override
    public void uploadBackground() {
        if (checkStoragePermissions()) {
            IntentUtils.openStorageIntent(getActivity(), "image/*", Requests.EXTERNAL_STORAGE_REQUEST);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    Requests.EXTERNAL_STORAGE_REQUEST);
        }
    }

    @Override
    public void openImage(String path) {
        IntentUtils.openImage(getActivity().getApplicationContext(), path);
    }

    @Override
    public void createEmail(int subjectResId, int messageResId, String attachmentPath) {
        IntentUtils.createEmail(getActivity().getApplicationContext(), getString(subjectResId), getString(messageResId), attachmentPath);
    }

    @Override
    public void clear() {
        lampsHolder.removeAllViews();
        backgroundView.setImageBitmap(null);
    }

    @Override
    public void showCreateSessionFragment() {
        ((MainActivity) getActivity()).showCreateSessionFragment();
    }

    public void updateBackground(Intent data) {
        Bitmap bitmap = ImageUtils.bitmapFromIntent(getActivity().getApplicationContext(), data, backgroundView.getWidth(), backgroundView.getHeight());
        backgroundView.setImageBitmap(bitmap);
    }

    //-------------------------------------------------------------
    // Private

    private void initPreloader() {
        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private boolean checkStoragePermissions() {
        return ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}
