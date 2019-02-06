package com.zugara.atproj.lampsplus.ui.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.presenters.InvoicePresenter;
import com.zugara.atproj.lampsplus.ui.fragments.base.BaseFragment;
import com.zugara.atproj.lampsplus.views.InvoiceView;

import butterknife.BindView;

/**
 * Created by andre on 25-Jan-19.
 */

public class InvoiceFragment extends BaseFragment implements InvoiceView {

    @BindView(R.id.invoiceView)
    ImageView invoiceView;

    private InvoicePresenter invoicePresenter;
    private ProgressDialog progressDialog;

    //-------------------------------------------------------------
    // Lifecycle override

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invoice, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPreloader();

        invoicePresenter = new InvoicePresenter(getActivity().getApplicationContext(), this);
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
    // InvoiceView

    @Override
    public void setInvoiceBitmap(Bitmap bitmap) {
        invoiceView.setImageBitmap(bitmap);
    }

    @Override
    public void show() {
        getView().setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        getView().setVisibility(View.GONE);
    }

    @Override
    public void clear() {
        getView().setDrawingCacheEnabled(false);
    }

    //-------------------------------------------------------------
    // Private methods

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
