package com.zugara.atproj.lampsplus.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.draganddrop.DragManager;
import com.zugara.atproj.lampsplus.draganddrop.DropManager;
import com.zugara.atproj.lampsplus.model.Lamp;
import com.zugara.atproj.lampsplus.presenters.CanvasPresenter;
import com.zugara.atproj.lampsplus.selection.Selectable;
import com.zugara.atproj.lampsplus.selection.SelectorManager;
import com.zugara.atproj.lampsplus.ui.fragments.base.BaseFragment;
import com.zugara.atproj.lampsplus.ui.view.GlowImage;
import com.zugara.atproj.lampsplus.ui.view.LampImageView;
import com.zugara.atproj.lampsplus.utils.ImageUtils;
import com.zugara.atproj.lampsplus.utils.IntentUtils;
import com.zugara.atproj.lampsplus.utils.Requests;
import com.zugara.atproj.lampsplus.views.CanvasView;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by andre on 15-Dec-18.
 */

public class CanvasFragment extends BaseFragment implements CanvasView {

    @Inject
    Picasso picasso;

    @BindView(R.id.screenshotHolder)
    LinearLayout screenshotHolder;

    @BindView(R.id.lampsHolder)
    RelativeLayout lampsHolder;

    @BindView(R.id.glowView)
    ImageView shadowView;

    @BindView(R.id.glowHolder)
    RelativeLayout glowHolder;

    @BindView(R.id.backgroundView)
    ImageView backgroundView;

    private ProgressDialog progressDialog;

    private CanvasPresenter canvasPresenter;

    private SelectorManager selectorManager;
    private DragManager.DragEventListener dragEventListener;

    private HashMap<Object, GlowImage> glows = new HashMap<>();

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

        //dragEventListener = new DragManager.DragEventListener();
        //lampsHolder.setOnDragListener(dragEventListener);

        lampsHolder.setOnDragListener(new DropManager(new DropManager.OnDropListener() {
            @Override
            public void onDrop(Object localState, float dropX, float dropY) {
                ImageView imageView = (ImageView) localState;
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                Lamp lamp = (Lamp) imageView.getTag();

                LampImageView lampView = new LampImageView(getContext(), bitmap);


                lampsHolder.addView(lampView);
                lampView.move(dropX, dropY);
            }
        }));

        selectorManager = new SelectorManager();

        lampsHolder.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                final LampImageView image = (LampImageView) child;
                image.setBound(0,0,lampsHolder.getWidth()-100, lampsHolder.getHeight()-200);
                image.setSelectorManager(selectorManager);
                canvasPresenter.changeNumOfChildren(lampsHolder.getChildCount());
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                LampImageView image = (LampImageView) child;
                selectorManager.removeItem(image);
                canvasPresenter.changeNumOfChildren(lampsHolder.getChildCount());
            }
        });

        canvasPresenter = new CanvasPresenter(getActivity(), this);
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
    public void enable() {
        selectorManager.setEnabled(true);
        dragEventListener.setEnabled(true);

        for (int i = 0; i < lampsHolder.getChildCount(); i++) {
            LampImageView image = (LampImageView) lampsHolder.getChildAt(i);
            image.enableTouch();
        }
    }

    @Override
    public void disable() {
        selectorManager.setEnabled(false);
        dragEventListener.setEnabled(false);
        for (int i = 0; i < lampsHolder.getChildCount(); i++) {
            LampImageView image = (LampImageView) lampsHolder.getChildAt(i);
            image.disableTouch();
        }
    }

    public SelectorManager getSelectorManager() {
        return selectorManager;
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

    public void getLampData(List<Lamp> lamps, List<Matrix> matrices, List<Boolean> mirroredList, List<Integer> sourceWidthList) {
        for (int i = 0; i < lampsHolder.getChildCount(); i++) {
            LampImageView image = (LampImageView) lampsHolder.getChildAt(i);

            lamps.add((Lamp) image.getTag());
            matrices.add(image.getImageMatrix());
            mirroredList.add(image.isMirrored());
            sourceWidthList.add(((BitmapDrawable)image.getDrawable()).getBitmap().getWidth());
        }
    }

    @Override
    public void setGlowBitmap(Bitmap bitmap) {
        shadowView.setImageBitmap(bitmap);
    }

    @Override
    public Bitmap getGlowSourceBitmap() {
        glowHolder.setDrawingCacheEnabled(true);
        Bitmap bitmap = glowHolder.getDrawingCache();
        return bitmap;
    }

    @Override
    public int getWidth() {
        return shadowView.getWidth();
    }

    @Override
    public int getHeight() {
        return shadowView.getHeight();
    }

    @Override
    public void setGlows(List<Object> sources, List<Matrix> matrices) {
        for (int i  = 0; i < sources.size(); i++) {
            Object source = sources.get(i);
            Matrix matrix = matrices.get(i);
            GlowImage glowImage = new GlowImage(getActivity().getApplicationContext());
            glowImage.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            glowHolder.addView(glowImage);
            if (source instanceof File) {
                picasso.load((File) source).into(glowImage);
            }
            glowImage.setImageMatrix(matrix);
        }
    }

    @Override
    public void clearGlow() {
        glowHolder.setDrawingCacheEnabled(false);
        glowHolder.removeAllViews();
    }


    @Override
    public void deleteLamp(Selectable item) {
        lampsHolder.removeView((View)item);
    }

    @Override
    public void addLamp(Selectable item) {
        lampsHolder.addView((View) item);
    }

    public void updateBackground(Intent data) {
        Bitmap bitmap = ImageUtils.bitmapFromIntent(getActivity().getApplicationContext(), data, backgroundView.getWidth(), backgroundView.getHeight());
        backgroundView.setImageBitmap(bitmap);
    }

    @Override
    public Bitmap createScreenshot() {
        screenshotHolder.setDrawingCacheEnabled(true);
        Bitmap bitmap = screenshotHolder.getDrawingCache();
        return bitmap;
    }

    @Override
    public void hide() {
        getView().setVisibility(View.GONE);
    }

    @Override
    public void show() {
        getView().setVisibility(View.VISIBLE);
    }

    @Override
    public void clear() {
        screenshotHolder.setDrawingCacheEnabled(false);
        lampsHolder.removeAllViews();
        backgroundView.setImageBitmap(null);
        glowHolder.removeAllViews();
        glows.clear();
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
