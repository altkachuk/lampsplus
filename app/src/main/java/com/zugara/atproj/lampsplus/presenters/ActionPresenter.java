package com.zugara.atproj.lampsplus.presenters;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.model.Lamp;
import com.zugara.atproj.lampsplus.model.singleton.SessionContext;
import com.zugara.atproj.lampsplus.views.ActionView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andre on 27-Dec-18.
 */

public class ActionPresenter {

    private final String TAG = "ActionPresenter";

    private ActionView actionView;
    private SessionContext sessionContext;

    public ActionPresenter(ActionView actionView, SessionContext sessionContext) {
        this.actionView = actionView;
        this.sessionContext = sessionContext;

        actionView.gotoCanvasState();
        actionView.hideSessionButtons();
    }

    public void continueAction() {
        actionView.gotoCanvasState();
    }

    public void complete(List<Lamp> lampList) {
        actionView.gotoCompleteState();

        String invoice = buildInvoice(lampList);
        actionView.setInvoice(invoice);
        Log.d(TAG, invoice);
    }

    public void download(Bitmap srcBitmap) {
        actionView.showPreloader();

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        path += "/" + sessionContext.getSessionName();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        path += "_" + timeStamp;
        path += ".jpg";

        Bitmap bitmap = Bitmap.createBitmap(srcBitmap);
        if (saveImage(path, bitmap)) {
            actionView.hidePreloader();
            sessionContext.setLastImagePath(path);

            actionView.hideCanvasButtons();
            actionView.showSessionButtons();
        } else {
            actionView.hidePreloader();
            actionView.showErrorMessage(R.string.something_is_wrong);
        }
    }

    public void openImage() {
        actionView.openImage(sessionContext.getLastImagePath());
    }

    public void sendByEmail() {
        actionView.createEmail(R.string.email_subject, R.string.email_message, sessionContext.getLastImagePath());
    }

    public void finish() {
        actionView.gotoCanvasState();
        actionView.hideSessionButtons();
        actionView.showCanvasButtons();
    }

    public void newSession() {
        finish();
        actionView.showCreateSessionFragment();
    }

    private String buildInvoice(List<Lamp> lampList) {
        HashMap<String, String> nameList = new HashMap<>();
        HashMap<String, Float> priceList = new HashMap<>();
        HashMap<String, Integer> quantityList = new HashMap<>();
        float totalPrice = 0f;
        for (Lamp lamp : lampList) {
            if (lamp != null) {
                String id = lamp.getId();
                nameList.put(lamp.getId(), lamp.getId());
                priceList.put(lamp.getId(), lamp.getPrice());
                int qnt = quantityList.containsKey(id) ? quantityList.get(id) + 1 : 1;
                quantityList.put(id, qnt);
                totalPrice += lamp.getPrice();
            }
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
