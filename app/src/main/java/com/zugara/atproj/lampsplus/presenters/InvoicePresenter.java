package com.zugara.atproj.lampsplus.presenters;

import android.content.Context;

import com.zugara.atproj.lampsplus.events.ClearEvent;
import com.zugara.atproj.lampsplus.events.CreateScreenshotEvent;
import com.zugara.atproj.lampsplus.events.GotoDesignEvent;
import com.zugara.atproj.lampsplus.events.GotoInvoiceEvent;
import com.zugara.atproj.lampsplus.model.InvoiceItem;
import com.zugara.atproj.lampsplus.model.singleton.SessionContext;
import com.zugara.atproj.lampsplus.views.InvoiceView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

/**
 * Created by andre on 25-Jan-19.
 */

public class InvoicePresenter extends BasePresenter {

    @Inject
    SessionContext sessionContext;

    private InvoiceView invoiceView;

    private NumberFormat currencyFormat;

    public InvoicePresenter(Context context, InvoiceView invoiceView) {
        super(context);
        EventBus.getDefault().register(this);

        this.invoiceView = invoiceView;

        invoiceView.hide();

        currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateScreenshotEvent(CreateScreenshotEvent event) {
        invoiceView.showPreloader();

        invoiceView.setDataProvider(sessionContext.getInvoiceItems());
        float total = 0f;
        for (InvoiceItem invoiceItem : sessionContext.getInvoiceItems()) {
            total += invoiceItem.getTotalPrice();
        }
        invoiceView.setTotal(currencyFormat.format(total));
        invoiceView.setTax("0");
        invoiceView.setOrderTotal(currencyFormat.format(total));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                invoiceView.hidePreloader();
                sessionContext.setInvoiceBitmap(invoiceView.createScreenshot());
            }
        },250);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGotoInvoiceEvent(GotoInvoiceEvent event) {
        invoiceView.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGotoDesignEvent(GotoDesignEvent event) {
        invoiceView.hide();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClearEvent(ClearEvent event) {
        invoiceView.clear();
    }
}
