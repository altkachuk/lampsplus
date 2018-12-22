package com.zugara.atproj.lampsplus.presenters;

import android.graphics.Bitmap;
import android.os.Environment;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.drag.DragManager;
import com.zugara.atproj.lampsplus.drag.IDraggable;
import com.zugara.atproj.lampsplus.model.singleton.SessionContext;
import com.zugara.atproj.lampsplus.selection.ISelectable;
import com.zugara.atproj.lampsplus.selection.SelectorManager;
import com.zugara.atproj.lampsplus.utils.DateUtil;
import com.zugara.atproj.lampsplus.utils.FileUtil;
import com.zugara.atproj.lampsplus.views.CanvasView;

/**
 * Created by andre on 15-Dec-18.
 */

public class CanvasPresenter {

    private CanvasView canvasView;
    private SessionContext sessionContext;
    private SelectorManager selectorManager;
    DragManager.DragEventListener dragEventListener;

    public CanvasPresenter(CanvasView canvasView, SessionContext sessionContext, SelectorManager selectorManager, DragManager.DragEventListener dragEventListener) {
        this.canvasView = canvasView;
        this.sessionContext = sessionContext;
        this.selectorManager = selectorManager;
        this.dragEventListener = dragEventListener;

        canvasView.gotoCanvasState();
        canvasView.hideSessionButtons();
    }

    public void upload() {
        canvasView.uploadBackground();
    }

    public void copy() {
        ISelectable item = (ISelectable) ((IDraggable)selectorManager.getFrontItem()).clone();
        canvasView.addLamp(item);
        selectorManager.addItem(item);
    }

    public void delete() {
        ISelectable item = selectorManager.removeFrontItem();
        if (item != null)
            canvasView.deleteLamp(item);
    }

    public void complete() {
        selectorManager.setEnabled(false);
        dragEventListener.setEnabled(false);
        canvasView.enableLampButtons(false);
        canvasView.gotoCompleteState();
    }

    public void continueAction() {
        selectorManager.setEnabled(true);
        dragEventListener.setEnabled(true);
        canvasView.enableLampButtons(selectorManager.getFrontItem() != null);
        canvasView.gotoCanvasState();
    }

    public void download(Bitmap bitmap) {
        canvasView.showPreloader();

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        path += "/" + sessionContext.getSessionName();
        path += "_" + DateUtil.getCurrentDateTime().replace(" ", "_");
        path += ".jpg";

        if (FileUtil.saveImage(path, bitmap)) {
            canvasView.hidePreloader();
            sessionContext.setLastImagePath(path);
            canvasView.hideCanvasButtons();
            canvasView.showSessionButtons();
        } else {
            canvasView.hidePreloader();
            canvasView.showErrorMessage(R.string.something_is_wrong);
        }
    }

    public void open() {
        canvasView.openImage(sessionContext.getLastImagePath());
    }

    public void send() {
        canvasView.createEmail(R.string.email_subject, R.string.email_message, sessionContext.getLastImagePath());
    }

    public void finish() {
        canvasView.clear();
        selectorManager.setEnabled(true);
        dragEventListener.setEnabled(true);
        canvasView.gotoCanvasState();
        canvasView.hideSessionButtons();
        canvasView.showCanvasButtons();
    }

    public void changeNumOfChildren(int count) {
        canvasView.enableLampButtons(count > 0);
    }
}
