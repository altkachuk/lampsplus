package com.zugara.atproj.lampsplus.ui.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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

import com.squareup.picasso.Picasso;
import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.presenters.CanvasPresenter;
import com.zugara.atproj.lampsplus.utils.ImageUtils;
import com.zugara.atproj.lampsplus.utils.IntentUtils;
import com.zugara.atproj.lampsplus.utils.Requests;
import com.zugara.atproj.lampsplus.views.CanvasView;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by andre on 15-Dec-18.
 */

public class CanvasFragment extends LampsCanvasFragment implements CanvasView {

    @Inject
    Picasso picasso;

    @BindView(R.id.screenshotHolder)
    LinearLayout screenshotHolder;

    @BindView(R.id.glowView)
    ImageView shadowView;

    @BindView(R.id.backgroundView)
    ImageView backgroundView;

    private CanvasPresenter canvasPresenter;

    //-------------------------------------------------------------
    // Lifecycle override

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_canvas, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        canvasPresenter = new CanvasPresenter(getContext(), this);
    }

    //-------------------------------------------------------------
    // CanvasView methods

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

    public void updateShadow(float shadow) {
        float alpha = 255f * shadow;
        int color = Color.argb((int)alpha, 0, 0, 0);

        Bitmap shadowBitmap = Bitmap.createBitmap(shadowView.getWidth(), shadowView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas shadowCanvas = new Canvas(shadowBitmap);
        shadowCanvas.drawColor(color);

        if (shadow == 0f) {
            shadowView.setImageBitmap(shadowBitmap);
        } else {
            glowHolder.setDrawingCacheEnabled(true);
            Bitmap glowBitmap = glowHolder.getDrawingCache();

            Bitmap newShadowBitmap = Bitmap.createBitmap(glowBitmap.getWidth(), glowBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newShadowBitmap);
            canvas.drawBitmap(glowBitmap, 0, 0, null);

            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
            canvas.drawBitmap(shadowBitmap, 0, 0, paint);


            Bitmap warmLightBitmap = Bitmap.createBitmap(glowBitmap.getWidth(), glowBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas warmLightCanvas = new Canvas(warmLightBitmap);
            warmLightCanvas.drawColor(Color.argb(100, 255, 197, 143));

            Bitmap resultWarmLightBitmap = Bitmap.createBitmap(glowBitmap.getWidth(), glowBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas resultWarmLightCanvas = new Canvas(resultWarmLightBitmap);
            resultWarmLightCanvas.drawBitmap(glowBitmap, 0, 0, null);

            Paint paint2 = new Paint();
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            resultWarmLightCanvas.drawBitmap(warmLightBitmap, 0, 0, paint2);


            Paint paint3 = new Paint();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
            canvas.drawBitmap(resultWarmLightBitmap, 0, 0, paint3);

            shadowView.setImageBitmap(newShadowBitmap);
        }
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
        super.clear();
        screenshotHolder.setDrawingCacheEnabled(false);
        backgroundView.setImageBitmap(null);
    }

    //-------------------------------------------------------------
    // Private

    private boolean checkStoragePermissions() {
        return ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}
