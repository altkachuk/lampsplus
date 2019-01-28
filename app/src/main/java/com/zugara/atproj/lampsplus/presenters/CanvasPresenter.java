package com.zugara.atproj.lampsplus.presenters;

import android.content.Context;
import android.graphics.Matrix;

import com.zugara.atproj.lampsplus.drag.IDraggable;
import com.zugara.atproj.lampsplus.events.ChangeLampsEvent;
import com.zugara.atproj.lampsplus.events.ChangeShadowEvent;
import com.zugara.atproj.lampsplus.events.ClearEvent;
import com.zugara.atproj.lampsplus.events.CopyLampEvent;
import com.zugara.atproj.lampsplus.events.CreateScreenshotEvent;
import com.zugara.atproj.lampsplus.events.DeleteLampEvent;
import com.zugara.atproj.lampsplus.events.DisableCanvasEvent;
import com.zugara.atproj.lampsplus.events.EnableCanvasEvent;
import com.zugara.atproj.lampsplus.events.GotoDesignEvent;
import com.zugara.atproj.lampsplus.events.GotoInvoiceEvent;
import com.zugara.atproj.lampsplus.events.MirrorLampEvent;
import com.zugara.atproj.lampsplus.events.UploadBackgroundEvent;
import com.zugara.atproj.lampsplus.model.Lamp;
import com.zugara.atproj.lampsplus.model.singleton.SessionContext;
import com.zugara.atproj.lampsplus.selection.ISelectable;
import com.zugara.atproj.lampsplus.views.CanvasView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Created by andre on 15-Dec-18.
 */

public class CanvasPresenter extends BasePresenter {

    @Inject
    SessionContext sessionContext;

    private CanvasView canvasView;

    public CanvasPresenter(Context context, CanvasView canvasView) {
        super(context);
        EventBus.getDefault().register(this);

        this.canvasView = canvasView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateScreenshotEvent(CreateScreenshotEvent event) {
        sessionContext.setDesignBitmap(canvasView.createScreenshot());
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
        ((ISelectable)canvasView.getSelectorManager().getFrontItem()).mirror();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCopyLampEvent(CopyLampEvent event) {
        ISelectable item = (ISelectable) ((IDraggable)canvasView.getSelectorManager().getFrontItem()).clone();
        canvasView.addLamp(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteLampEvent(DeleteLampEvent event) {
        ISelectable item = canvasView.getSelectorManager().getFrontItem();
        if (item != null)
            canvasView.deleteLamp(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeShadowEvent(ChangeShadowEvent event) {
        canvasView.setShadow(event.getShadow());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClearEvent(ClearEvent event) {
        canvasView.clear();
    }


    public void addLamp(Object key, Lamp lamp) {
        sessionContext.addLampToInvoiceItem(lamp);

        // add effect
        if (lamp.getGlow() != null) {
            canvasView.addGlow(key, lamp.getGlow().getSource());
        }
    }

    public void removeLamp(Object key, Lamp lamp) {
        sessionContext.removeLampFromInvoiceItem(lamp);

        // remove effect
        if (lamp.getGlow() != null) {
            canvasView.removeGlow(key);
        }
    }

    public void changeNumOfChildren(int count) {
        EventBus.getDefault().post(new ChangeLampsEvent(count));
    }

    public void transformEffect(Object key, Matrix matrix) {
        canvasView.transformGlow(key, matrix);
    }

    public void mirrorEffect(Lamp lamp) {
        if (lamp.getGlow() != null) {
            canvasView.mirrorGlow(lamp.getId());
        }
    }
}
