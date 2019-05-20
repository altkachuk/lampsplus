package com.atproj.zugara.lampsplus.presenters;

import android.content.Context;

import com.atproj.zugara.lampsplus.events.BackToDesignEvent;
import com.atproj.zugara.lampsplus.events.CompleteDesignEvent;
import com.atproj.zugara.lampsplus.model.singleton.SessionContext;
import com.atproj.zugara.lampsplus.views.LampsCanvasView;
import com.atproj.zugara.lampsplus.events.ChangeLampsEvent;
import com.atproj.zugara.lampsplus.events.ClearEvent;
import com.atproj.zugara.lampsplus.events.CopyLampEvent;
import com.atproj.zugara.lampsplus.events.DeleteLampEvent;
import com.atproj.zugara.lampsplus.events.DisableCanvasEvent;
import com.atproj.zugara.lampsplus.events.EnableCanvasEvent;
import com.atproj.zugara.lampsplus.events.MirrorLampEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Created by andre on 15-Dec-18.
 */

public class LampsCanvasPresenter extends BaseFilePresenter {

    @Inject
    SessionContext sessionContext;

    private LampsCanvasView lampsCanvasView;

    public LampsCanvasPresenter(Context context, LampsCanvasView lampsCanvasView) {
        super(context);
        EventBus.getDefault().register(this);
        this.lampsCanvasView = lampsCanvasView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompleteDesignEvent(CompleteDesignEvent event) {
        sessionContext.setInvoiceItemsProvider(lampsCanvasView.getLamps());
        lampsCanvasView.updateGlows();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackToDesignEvent(BackToDesignEvent event) {
        ;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEnableCanvasEvent(EnableCanvasEvent event) {
        lampsCanvasView.enable();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDisableCanvasEvent(DisableCanvasEvent event) {
        lampsCanvasView.disable();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMirrorLampEvent(MirrorLampEvent event) {
        lampsCanvasView.mirrorTopItem();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCopyLampEvent(CopyLampEvent event) {
        lampsCanvasView.copyTopItem();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteLampEvent(DeleteLampEvent event) {
        lampsCanvasView.removeTopItem();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClearEvent(ClearEvent event) {
        lampsCanvasView.clear();
    }

    public void changeNumOfChildren(int count) {
        EventBus.getDefault().post(new ChangeLampsEvent(count));
    }
}
