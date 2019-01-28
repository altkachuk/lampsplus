package com.zugara.atproj.lampsplus.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.presenters.ActionsPresenter;
import com.zugara.atproj.lampsplus.ui.fragments.base.BaseFragment;
import com.zugara.atproj.lampsplus.utils.IntentUtils;
import com.zugara.atproj.lampsplus.views.ActionsView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by andre on 26-Jan-19.
 */

public class ActionsFragment extends BaseFragment implements ActionsView {

    @BindView(R.id.canvasButtonHolder)
    LinearLayout canvasButtonHolder;

    @BindView(R.id.sessionButtonHolder)
    LinearLayout sessionButtonHolder;

    @BindView(R.id.uploadButton)
    Button uploadButton;

    @BindView(R.id.mirrorButton)
    Button mirrorButton;

    @BindView(R.id.copyButton)
    Button copyButton;

    @BindView(R.id.deleteButton)
    Button deleteButton;

    @BindView(R.id.shadowButton)
    ImageView shadowButton;

    @BindView(R.id.completeButton)
    Button completeButton;

    @BindView(R.id.backButton)
    Button backButton;

    @BindView(R.id.nextButton)
    Button nextButton;

    @BindView(R.id.invoiceButton)
    Button invoiceButton;

    @BindView(R.id.designButton)
    Button designButton;

    private ActionsPresenter actionsPresenter;
    private ProgressDialog progressDialog;

    //-------------------------------------------------------------
    // Lifecycle override

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_actions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPreloader();

        actionsPresenter = new ActionsPresenter(getActivity().getApplicationContext(), this);
    }

    //-------------------------------------------------------------
    // Handlers

    @OnClick(R.id.uploadButton)
    public void onClickUploadButton() {
        actionsPresenter.upload();
    }

    @OnClick(R.id.mirrorButton)
    public void onClickMirrorButton() {
        actionsPresenter.mirror();
    }

    @OnClick(R.id.copyButton)
    public void onClickCopyButton() {
        actionsPresenter.copy();
    }

    @OnClick(R.id.deleteButton)
    public void onClickDeleteButton() {
        actionsPresenter.delete();
    }

    @OnClick(R.id.shadowButton)
    public void onClickShadowButon() {
        actionsPresenter.increaseShadow();
    }

    @OnClick(R.id.completeButton)
    public void onCompleteButton() {
        actionsPresenter.disableCanvas();
        actionsPresenter.complete();
    }

    @OnClick(R.id.backButton)
    public void onClickBackButton() {
        actionsPresenter.enableCanvas();
        actionsPresenter.clearShadow();
        actionsPresenter.back();
    }

    @OnClick(R.id.nextButton)
    public void onClickNextButton() {
        actionsPresenter.next();
    }

    @OnClick(R.id.invoiceButton)
    public void onClickInvoiceButton() {
        actionsPresenter.gotoInvoice();
    }

    @OnClick(R.id.designButton)
    public void onClickDesignButton() {
        actionsPresenter.gotoDesign();
    }

    @OnClick(R.id.sendButton)
    public void onClickSendButton() {
        actionsPresenter.sendByEmail();
    }

    @OnClick(R.id.printButton)
    public void onClickPrintButton() {
        actionsPresenter.print();
    }

    @OnClick(R.id.newSessionButton)
    public void onClickNewSessionButton() {
        actionsPresenter.enableCanvas();
        actionsPresenter.clear();
        actionsPresenter.newSession();
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
    // ActionsView methods

    @Override
    public void enableLampButtons(boolean enable) {
        mirrorButton.setEnabled(enable);
        copyButton.setEnabled(enable);
        deleteButton.setEnabled(enable);

        if (enable) {
            mirrorButton.setAlpha(0.75f);
            copyButton.setAlpha(0.75f);
            deleteButton.setAlpha(0.75f);
        } else {
            mirrorButton.setAlpha(1f);
            copyButton.setAlpha(1f);
            deleteButton.setAlpha(1f);
        }
    }

    @Override
    public void gotoCanvasState() {
        uploadButton.setVisibility(View.VISIBLE);
        mirrorButton.setVisibility(View.VISIBLE);
        copyButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        completeButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        shadowButton.setVisibility(View.GONE);
    }

    @Override
    public void gotoCompleteState() {
        uploadButton.setVisibility(View.GONE);
        mirrorButton.setVisibility(View.GONE);
        copyButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        completeButton.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
        shadowButton.setVisibility(View.VISIBLE);
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

    @Override
    public void hideSessionButtons() {
        sessionButtonHolder.setVisibility(View.GONE);
    }

    @Override
    public void showCreateSessionFragment() {
        getActivity().getFragmentManager().findFragmentById(R.id.createSessionFragment).getView().setVisibility(View.VISIBLE);
    }

    @Override
    public void updateShadowButton(float percent) {
        int buttonRes = -1;
        if (percent == 0) buttonRes = R.mipmap.light_100;
        else if (percent < 0.75f) buttonRes = R.mipmap.light_50;
        else buttonRes = R.mipmap.light_0;
        shadowButton.setImageResource(buttonRes);
    }

    @Override
    public void gotoInvoiceState() {
        invoiceButton.setVisibility(View.GONE);
        designButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void gotoDesignState() {
        invoiceButton.setVisibility(View.VISIBLE);
        designButton.setVisibility(View.GONE);
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
}
