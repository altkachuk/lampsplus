package com.zugara.atproj.lampsplus.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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
import com.zugara.atproj.lampsplus.drag.DragManager;
import com.zugara.atproj.lampsplus.drag.OnTransformListener;
import com.zugara.atproj.lampsplus.model.Lamp;
import com.zugara.atproj.lampsplus.presenters.ActionsPresenter;
import com.zugara.atproj.lampsplus.presenters.CanvasPresenter;
import com.zugara.atproj.lampsplus.selection.ISelectable;
import com.zugara.atproj.lampsplus.selection.SelectorManager;
import com.zugara.atproj.lampsplus.ui.fragments.base.BaseFragment;
import com.zugara.atproj.lampsplus.ui.view.DraggableImage;
import com.zugara.atproj.lampsplus.ui.view.GlowImage;
import com.zugara.atproj.lampsplus.utils.ImageUtils;
import com.zugara.atproj.lampsplus.utils.IntentUtils;
import com.zugara.atproj.lampsplus.utils.Requests;
import com.zugara.atproj.lampsplus.views.ActionsView;
import com.zugara.atproj.lampsplus.views.CanvasView;
import com.zugara.atproj.lampsplus.views.InvoiceView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    @BindView(R.id.shadowView)
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

        dragEventListener = new DragManager.DragEventListener();
        lampsHolder.setOnDragListener(dragEventListener);

        selectorManager = new SelectorManager();

        lampsHolder.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                final DraggableImage image = (DraggableImage) child;
                final Lamp lamp = (Lamp) image.getTag();
                image.setOnTransformListener(new OnTransformListener() {
                    @Override
                    public void onMatrixChange(Matrix matrix) {
                        canvasPresenter.transformEffect(image, matrix);
                    }

                    @Override
                    public void onMirror() {
                        canvasPresenter.mirrorEffect(lamp);
                    }
                });
                image.setSelectorManager(selectorManager);
                canvasPresenter.changeNumOfChildren(lampsHolder.getChildCount());
                canvasPresenter.addLamp(child, (Lamp) image.getTag());
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                DraggableImage image = (DraggableImage) child;
                selectorManager.removeItem(image);
                canvasPresenter.changeNumOfChildren(lampsHolder.getChildCount());
                canvasPresenter.removeLamp(child, (Lamp) image.getTag());
            }
        });

        canvasPresenter = new CanvasPresenter(getActivity().getApplicationContext(), this);
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
    }

    @Override
    public void disable() {
        selectorManager.setEnabled(false);
        dragEventListener.setEnabled(false);
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

    @Override
    public void setShadow(float percent) {
        int color = Color.argb((int)(255f*percent), 0, 0, 0);
        Bitmap shadowBitmap = Bitmap.createBitmap(shadowView.getWidth(), shadowView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas shadowCanvas = new Canvas(shadowBitmap);
        shadowCanvas.drawColor(color);

        glowHolder.setDrawingCacheEnabled(true);
        if (percent == 0f) {
            shadowView.setImageBitmap(shadowBitmap);
        } else {
            drawGlow(shadowBitmap);
        }
    }

    private void drawGlow(Bitmap shadowBitmap) {
        glowHolder.setDrawingCacheEnabled(true);
        Bitmap glowBitmap = glowHolder.getDrawingCache();

        Bitmap result = Bitmap.createBitmap(shadowView.getWidth(), shadowView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(glowBitmap, 0, 0, null);

        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(shadowBitmap, 0, 0, paint);

        shadowView.setImageBitmap(result);

        glowHolder.setDrawingCacheEnabled(false);
    }

    @Override
    public void addGlow(Object key, Object source) {
        GlowImage glowImage = new GlowImage(getActivity().getApplicationContext());
        glowImage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        glowHolder.addView(glowImage);
        if (source instanceof File) {
            picasso.load((File) source).into(glowImage);
        }

        glows.put(key, glowImage);
    }

    @Override
    public void removeGlow(Object key) {
        glowHolder.removeView(glows.get(key));
    }

    @Override
    public void transformGlow(Object key, Matrix matrix) {
        if (glows.containsKey(key)) {
            glows.get(key).setImageMatrix(matrix);
        }
    }

    @Override
    public void mirrorGlow(String id) {
        glows.get(id).mirror();
    }


    @Override
    public void deleteLamp(ISelectable item) {
        lampsHolder.removeView((View)item);
    }

    @Override
    public void addLamp(ISelectable item) {
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
