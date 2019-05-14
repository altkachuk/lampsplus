package com.zugara.atproj.lampsplus.presenters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.zugara.atproj.lampsplus.draganddrop.Draggable;
import com.zugara.atproj.lampsplus.events.BackToDesignEvent;
import com.zugara.atproj.lampsplus.events.ChangeLampsEvent;
import com.zugara.atproj.lampsplus.events.ChangeShadowEvent;
import com.zugara.atproj.lampsplus.events.ClearEvent;
import com.zugara.atproj.lampsplus.events.CompleteLightEvent;
import com.zugara.atproj.lampsplus.events.CopyLampEvent;
import com.zugara.atproj.lampsplus.events.CompleteDesignEvent;
import com.zugara.atproj.lampsplus.events.DeleteLampEvent;
import com.zugara.atproj.lampsplus.events.DisableCanvasEvent;
import com.zugara.atproj.lampsplus.events.EnableCanvasEvent;
import com.zugara.atproj.lampsplus.events.GotoDesignEvent;
import com.zugara.atproj.lampsplus.events.GotoInvoiceEvent;
import com.zugara.atproj.lampsplus.events.MirrorLampEvent;
import com.zugara.atproj.lampsplus.events.UploadBackgroundEvent;
import com.zugara.atproj.lampsplus.model.Lamp;
import com.zugara.atproj.lampsplus.model.singleton.SessionContext;
import com.zugara.atproj.lampsplus.selection.Mirrorable;
import com.zugara.atproj.lampsplus.selection.Selectable;
import com.zugara.atproj.lampsplus.views.CanvasView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by andre on 15-Dec-18.
 */

public class CanvasPresenter extends BaseFilePresenter {

    @Inject
    SessionContext sessionContext;

    private Activity activity;
    private CanvasView canvasView;

    public CanvasPresenter(Activity activity, CanvasView canvasView) {
        super(activity.getApplicationContext());
        EventBus.getDefault().register(this);

        this.activity = activity;
        this.canvasView = canvasView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompleteDesignEvent(CompleteDesignEvent event) {
        List<Lamp> lamps = new ArrayList<>();
        List<Matrix> matrices = new ArrayList<>();
        List<Boolean> mirroredList = new ArrayList<>();
        List<Integer> sourceWidthList = new ArrayList<>();
        canvasView.getLampData(lamps, matrices, mirroredList, sourceWidthList);

        sessionContext.setInvoiceItemsProvider(lamps);

        canvasView.clearGlow();

        List<Object> glowSources = new ArrayList<>();
        List<Matrix> glowMatrices = new ArrayList<>();

        for (int i = 0; i < lamps.size(); i++) {
            if (lamps.get(i).getGlow() != null) {
                final Lamp lamp = lamps.get(i);
                Matrix matrix = new Matrix();
                float[] values = new float[9];
                matrices.get(i).getValues(values);
                matrix.setValues(values);

                Matrix temp = new Matrix();



                if (mirroredList.get(i))
                    temp.postRotate(-lamp.getRotation(), 240, 106);
                else
                    temp.postRotate(lamp.getRotation(), 240, 106);

                temp.postScale(lamp.getScale(), lamp.getScale());

                // move to center
                temp.postTranslate(-240 * lamp.getScale(), -106 * lamp.getScale());

                    // move to right position
                if (mirroredList.get(i))
                    temp.postTranslate(sourceWidthList.get(i) - lamp.getPoint().x, lamp.getPoint().y);
                else
                    temp.postTranslate(lamp.getPoint().x, lamp.getPoint().y);

                matrix.preConcat(temp);

                glowSources.add(lamp.getGlow().getSource());
                glowMatrices.add(matrix);
            }
        }

        canvasView.setGlows(glowSources, glowMatrices);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompleteLightEvent(CompleteLightEvent event) {
        Bitmap bitmap = canvasView.createScreenshot();
        String designPath = downloadImage(bitmap, "design");
        sessionContext.setDesignPath(designPath);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackToDesignEvent(BackToDesignEvent event) {
        ;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGotoInvoiceEvent(GotoInvoiceEvent event) {
        canvasView.hide();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGotoDesignEvent(GotoDesignEvent event) {
        canvasView.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEnableCanvasEvent(EnableCanvasEvent event) {
        canvasView.enable();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDisableCanvasEvent(DisableCanvasEvent event) {
        canvasView.disable();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUploadBackgroundEvent(UploadBackgroundEvent event) {
        canvasView.uploadBackground();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMirrorLampEvent(MirrorLampEvent event) {
        ((Mirrorable)canvasView.getSelectorManager().getFrontItem()).mirror();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCopyLampEvent(CopyLampEvent event) {
        Selectable item = (Selectable) ((Draggable)canvasView.getSelectorManager().getFrontItem()).clone();
        canvasView.addLamp(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteLampEvent(DeleteLampEvent event) {
        Selectable item = canvasView.getSelectorManager().getFrontItem();
        if (item != null)
            canvasView.deleteLamp(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeShadowEvent(ChangeShadowEvent event) {
        float alpha = 255f * event.getShadow();
        int color = Color.argb((int)alpha, 0, 0, 0);

        Bitmap shadowBitmap = Bitmap.createBitmap(canvasView.getWidth(), canvasView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas shadowCanvas = new Canvas(shadowBitmap);
        shadowCanvas.drawColor(color);

        if (event.getShadow() == 0f) {
            canvasView.setGlowBitmap(shadowBitmap);
        } else {

            Bitmap glowBitmap = canvasView.getGlowSourceBitmap();

            Bitmap result = Bitmap.createBitmap(glowBitmap.getWidth(), glowBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
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

            canvasView.setGlowBitmap(result);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClearEvent(ClearEvent event) {
        canvasView.clear();
    }

    public void changeNumOfChildren(int count) {
        EventBus.getDefault().post(new ChangeLampsEvent(count));
    }
}
