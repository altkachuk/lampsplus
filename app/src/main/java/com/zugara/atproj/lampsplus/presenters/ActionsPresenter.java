package com.zugara.atproj.lampsplus.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.zugara.atproj.lampsplus.R;
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
import com.zugara.atproj.lampsplus.model.singleton.SessionContext;
import com.zugara.atproj.lampsplus.utils.IntentUtils;
import com.zugara.atproj.lampsplus.views.ActionsView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private List<String> attachments = new ArrayList<>();

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
        if (shadowPercent == 0f) shadowPercent = 0.6f;
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
        for (String path : attachments) {
            File file = new File(path);
            file.delete();
        }
        attachments.clear();

        EventBus.getDefault().post(new ClearEvent());
    }

    public void back() {
        actionView.gotoCanvasState();
    }

    public void complete() {
        actionView.gotoCompleteState();
    }

    public void next() {
        gotoInvoice();
        EventBus.getDefault().post(new CreateScreenshotEvent());

        actionView.hideCanvasButtons();
        actionView.showSessionButtons();
    }

    public String downloadImage(final Bitmap srcBitmap, String prefix) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        path += "/" + sessionContext.getSessionName() + "_" + prefix;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        path += "_" + timeStamp;
        path += ".jpg";


        final String fPath = path;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(srcBitmap);
                saveImage(fPath, bitmap);
            }
        }).start();
        return path;
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
        if (attachments.size() == 0) {
            String designPath = downloadImage(sessionContext.getDesignBitmap(), "design");
            if (designPath != null) {
                attachments.add(designPath);
            }
            String invoicePath = downloadImage(sessionContext.getInvoiceBitmap(), "invoice");
            if (invoicePath != null) {
                attachments.add(invoicePath);
            }
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                actionView.hidePreloader();
                IntentUtils.createEmail(context, context.getString(R.string.email_subject), context.getString(R.string.email_message), attachments);
            }
        }, 1000);

    }

    public void print() {
        actionView.showPreloader();
        if (attachments.size() == 0) {
            String designPath = downloadImage(sessionContext.getDesignBitmap(), "design");
            if (designPath != null) {
                attachments.add(designPath);
            }
            String invoicePath = downloadImage(sessionContext.getInvoiceBitmap(), "invoice");
            if (invoicePath != null) {
                attachments.add(invoicePath);
            }
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                actionView.hidePreloader();
                // TODO: print
            }
        }, 1000);
    }

    public void finish() {
        actionView.gotoCanvasState();
        actionView.hideSessionButtons();
        actionView.showCanvasButtons();
        gotoDesign();
    }

    public void newSession() {
        finish();
        actionView.showCreateSessionFragment();
    }

    private boolean saveImage(String path, Bitmap bitmap) {
        File file = new File(path);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.getMessage());
            return false;
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            return false;
        }

        return true;
    }
}
