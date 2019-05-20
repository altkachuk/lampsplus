package com.atproj.zugara.lampsplus.presenters;

import android.content.Context;
import android.graphics.Bitmap;

import com.atproj.zugara.lampsplus.events.BackToDesignEvent;
import com.atproj.zugara.lampsplus.events.CompleteLightEvent;
import com.atproj.zugara.lampsplus.model.singleton.SessionContext;
import com.atproj.zugara.lampsplus.events.ChangeLampsEvent;
import com.atproj.zugara.lampsplus.events.ChangeShadowEvent;
import com.atproj.zugara.lampsplus.events.GotoDesignEvent;
import com.atproj.zugara.lampsplus.events.GotoInvoiceEvent;
import com.atproj.zugara.lampsplus.events.UploadBackgroundEvent;
import com.atproj.zugara.lampsplus.views.CanvasView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * Created by andre on 15-Dec-18.
 */

public class CanvasPresenter extends BaseFilePresenter {

    @Inject
    SessionContext sessionContext;

    private CanvasView canvasView;

    public CanvasPresenter(Context context, CanvasView canvasView) {
        super(context);
        EventBus.getDefault().register(this);
        this.canvasView = canvasView;
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
    public void onUploadBackgroundEvent(UploadBackgroundEvent event) {
        canvasView.uploadBackground();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeShadowEvent(ChangeShadowEvent event) {
        canvasView.updateShadow(event.getShadow());
    }

    public void changeNumOfChildren(int count) {
        EventBus.getDefault().post(new ChangeLampsEvent(count));
    }
}
