package com.zugara.atproj.lampsplus.presenters;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.drag.DragManager;
import com.zugara.atproj.lampsplus.drag.IDraggable;
import com.zugara.atproj.lampsplus.model.Lamp;
import com.zugara.atproj.lampsplus.model.singleton.SessionContext;
import com.zugara.atproj.lampsplus.selection.ISelectable;
import com.zugara.atproj.lampsplus.selection.SelectorManager;
import com.zugara.atproj.lampsplus.views.CanvasView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public class CanvasPresenter {

    private final String TAG = "CanvasPresenter";

    private CanvasView canvasView;
    private SessionContext sessionContext;
    private SelectorManager selectorManager;
    DragManager.DragEventListener dragEventListener;

    private List<Lamp> lampList = new ArrayList<>();

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
    }

    public void delete() {
        ISelectable item = selectorManager.getFrontItem();
        if (item != null)
            canvasView.deleteLamp(item);
    }

    public void complete() {
        selectorManager.setEnabled(false);
        dragEventListener.setEnabled(false);
        canvasView.enableLampButtons(false);
        canvasView.gotoCompleteState();

        String invoice = buildInvoice();
        Log.d(TAG, invoice);
    }

    public void continueAction() {
        selectorManager.setEnabled(true);
        dragEventListener.setEnabled(true);
        canvasView.enableLampButtons(selectorManager.getFrontItem() != null);
        canvasView.gotoCanvasState();
    }

    public void download(Bitmap srcBitmap) {
        canvasView.showPreloader();

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        path += "/" + sessionContext.getSessionName();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        path += "_" + timeStamp;
        path += ".jpg";

        Bitmap bitmap = Bitmap.createBitmap(srcBitmap);
        if (saveImage(path, bitmap)) {
            canvasView.hidePreloader();
            sessionContext.setLastImagePath(path);
            canvasView.hideCanvasButtons();
            canvasView.showSessionButtons();
        } else {
            canvasView.hidePreloader();
            canvasView.showErrorMessage(R.string.something_is_wrong);
        }
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

    public void newSession() {
        finish();
        canvasView.showCreateSessionFragment();
    }

    public void changeNumOfChildren(int count) {
        canvasView.enableLampButtons(count > 0);
    }

    public void addTag(Object tag) {
        lampList.add((Lamp) tag);
    }

    public void removeTag(Object tag) {
        lampList.remove((Lamp) tag);
    }

    private String buildInvoice() {
        HashMap<String, String> nameList = new HashMap<>();
        HashMap<String, Float> priceList = new HashMap<>();
        HashMap<String, Integer> quantityList = new HashMap<>();
        float totalPrice = 0f;
        for (Lamp lamp : lampList) {
            String id = lamp.getId();
            nameList.put(lamp.getId(), lamp.getId());
            priceList.put(lamp.getId(), lamp.getPrice());
            int qnt = quantityList.containsKey(id) ? quantityList.get(id) + 1 : 1;
            quantityList.put(id, qnt);
            totalPrice += lamp.getPrice();
        }

        String newStrCh = "\n";
        String tabCh = "\t";

        String result = addSpaceToEnd("id", 15) + tabCh + addSpaceToEnd("prc", 10) + tabCh + addSpaceToEnd("qnt", 5) + newStrCh;



        for (String id : nameList.keySet()) {
            result += addSpaceToEnd(nameList.get(id), 15) + tabCh + addSpaceToEnd(priceList.get(id).toString(), 10) + tabCh + addSpaceToEnd(quantityList.get(id).toString(), 5) + newStrCh;
        }

        result += "Total: " + totalPrice;
        return result;
    }

    private String addSpaceToEnd(String src, int size) {
        if (src.length() >= size) {
            return src;
        }
        String space = new String(new char[size - src.length()]).replace("\0", " ");
        return src + space;
    }
}
