package com.atproj.zugara.lampsplus.presenters;

import android.app.Activity;
import android.content.Context;

import com.atproj.zugara.lampsplus.events.BackToDesignEvent;
import com.atproj.zugara.lampsplus.events.ChangeLampsEvent;
import com.atproj.zugara.lampsplus.events.ChangeShadowEvent;
import com.atproj.zugara.lampsplus.events.ClearEvent;
import com.atproj.zugara.lampsplus.events.CompleteDesignEvent;
import com.atproj.zugara.lampsplus.events.CompleteLightEvent;
import com.atproj.zugara.lampsplus.events.CopyLampEvent;
import com.atproj.zugara.lampsplus.events.DeleteLampEvent;
import com.atproj.zugara.lampsplus.events.DisableCanvasEvent;
import com.atproj.zugara.lampsplus.events.EnableCanvasEvent;
import com.atproj.zugara.lampsplus.events.GotoDesignEvent;
import com.atproj.zugara.lampsplus.events.GotoInvoiceEvent;
import com.atproj.zugara.lampsplus.events.MirrorLampEvent;
import com.atproj.zugara.lampsplus.events.UploadBackgroundEvent;
import com.atproj.zugara.lampsplus.model.singleton.SessionContext;
import com.atproj.zugara.lampsplus.utils.IntentUtils;
import com.atproj.zugara.lampsplus.R;
import com.atproj.zugara.lampsplus.views.ActionsView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

/**
 * Created by andre on 27-Dec-18.
 */

public class ActionsPresenter extends BasePresenter {

    private final String TAG = "ActionsPresenter";

    @Inject
    SessionContext sessionContext;

    private ActionsView actionView;
    private float shadowPercent = 0f;

    public ActionsPresenter(Context context, ActionsView actionView) {
        super(context);
        EventBus.getDefault().register(this);

        this.actionView = actionView;

        actionView.gotoDesignState();
        actionView.gotoCanvasState();
        actionView.hideSessionButtons();
        actionView.enableLampButtons(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeLampsEvent(ChangeLampsEvent event) {
        actionView.enableLampButtons(event.getCount() > 0);
    }

    public void enableCanvas() {
        EventBus.getDefault().post(new EnableCanvasEvent());
    }

    public void disableCanvas() {
        EventBus.getDefault().post(new DisableCanvasEvent());
    }

    public void upload() {
        EventBus.getDefault().post(new UploadBackgroundEvent());
    }

    public void mirror() {
        EventBus.getDefault().post(new MirrorLampEvent());
    }

    public void copy() {
        EventBus.getDefault().post(new CopyLampEvent());
    }

    public void delete() {
        EventBus.getDefault().post(new DeleteLampEvent());
    }

    public void increaseShadow() {
        if (shadowPercent == 0f) shadowPercent = 0.3f;
        else if (shadowPercent == 0.3f) shadowPercent = 0.6f;
        else if (shadowPercent == 0.6f) shadowPercent = 0.85f;
        else shadowPercent = 0f;

        actionView.updateShadowButton(shadowPercent);
        EventBus.getDefault().post(new ChangeShadowEvent(shadowPercent));
    }

    public void clearShadow() {
        shadowPercent = 0f;

        actionView.updateShadowButton(shadowPercent);
        EventBus.getDefault().post(new ChangeShadowEvent(shadowPercent));
    }

    public void clear() {
        sessionContext.clearInvoiceItems();
        clearShadow();

        EventBus.getDefault().post(new ClearEvent());
    }

    public void back() {
        actionView.gotoCanvasState();
    }

    public void complete() {
        actionView.gotoCompleteState();
        EventBus.getDefault().post(new CompleteDesignEvent());
    }

    public void next() {
        gotoInvoice();

        actionView.hideCanvasButtons();
        actionView.showSessionButtons();
        EventBus.getDefault().post(new CompleteLightEvent());
    }

    public void backToDesign() {
        gotoDesign();

        actionView.showCanvasButtons();
        actionView.hideSessionButtons();

        EventBus.getDefault().post(new BackToDesignEvent());
    }

    public void gotoInvoice() {
        EventBus.getDefault().post(new GotoInvoiceEvent());

        actionView.gotoInvoiceState();
    }

    public void gotoDesign() {
        EventBus.getDefault().post(new GotoDesignEvent());

        actionView.gotoDesignState();
    }

    public void sendByEmail() {
        actionView.showPreloader();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                actionView.hidePreloader();
                List<String> attachments = new ArrayList<>();
                attachments.add(sessionContext.getDesignPath());
                attachments.add(sessionContext.getInvoicePath());
                IntentUtils.createEmail(context, context.getString(R.string.email_subject), context.getString(R.string.email_message), attachments);
            }
        }, 200);

    }

    public void print(Activity activity) {
        List<String> images = new ArrayList<>();
        images.add(sessionContext.getDesignPath());
        images.add(sessionContext.getInvoicePath());
        IntentUtils.printImages(activity, images);
    }

    public void finish() {
        actionView.gotoCanvasState();
        actionView.hideSessionButtons();
        actionView.showCanvasButtons();
        gotoDesign();
    }

    public void newSession() {
        sessionContext.setDesignPath(null);
        sessionContext.setInvoicePath(null);
        finish();
        actionView.showCreateSessionFragment();
    }
}
