package com.atproj.zugara.lampsplus.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.atproj.zugara.lampsplus.R;
import com.atproj.zugara.lampsplus.events.BackToDesignEvent;
import com.atproj.zugara.lampsplus.events.ClearEvent;
import com.atproj.zugara.lampsplus.events.CompleteLightEvent;
import com.atproj.zugara.lampsplus.events.GotoDesignEvent;
import com.atproj.zugara.lampsplus.events.GotoInvoiceEvent;
import com.atproj.zugara.lampsplus.model.InvoiceItem;
import com.atproj.zugara.lampsplus.model.Source;
import com.atproj.zugara.lampsplus.model.singleton.SessionContext;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.atproj.zugara.lampsplus.utils.DpPxConverter;
import com.atproj.zugara.lampsplus.views.InvoiceView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by andre on 25-Jan-19.
 */

public class InvoicePresenter extends BaseFilePresenter {

    private static int PADDING = 20;
    private static int HORIZONTAL_MARGIN = 10;
    private static int VERTICAL_MARGIN = 20;
    private static int ITEM_HEIGHT = 40;

    private static int IMAGE_WIDTH = 40;
    private static int SKU_WIDTH = 50;
    private static int DESCRIPTION_WIDTH = 200;
    private static int QTY_WIDTH = 50;
    private static int PRICE_WIDTH = 50;
    private static int TOTAL_TEXT_WIDTH = 70;

    private static int TOTAL_TEXT_TOP_MARGIN = 20;



    @Inject
    SessionContext sessionContext;

    @Inject
    Picasso picasso;

    private InvoiceView invoiceView;

    private NumberFormat currencyFormat;

    private int invoiceIndex;

    private Bitmap invoiceBitmap;
    private Canvas invoiceCanvas;

    public InvoicePresenter(Context context, InvoiceView invoiceView) {
        super(context);
        EventBus.getDefault().register(this);

        this.invoiceView = invoiceView;

        invoiceView.hide();

        currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompleteLightEvent(CompleteLightEvent event) {
        invoiceView.showPreloader();
        invoiceIndex = 0;
        int bitmapWidth = PADDING * 2 + IMAGE_WIDTH + SKU_WIDTH + DESCRIPTION_WIDTH + QTY_WIDTH + PRICE_WIDTH + HORIZONTAL_MARGIN * 4;
        int bitmapHeight = PADDING * 2 + (ITEM_HEIGHT + VERTICAL_MARGIN) * (sessionContext.getInvoiceItems().size() + 2) + TOTAL_TEXT_TOP_MARGIN;
        invoiceBitmap = Bitmap.createBitmap(DpPxConverter.dpToPixel(context, bitmapWidth), DpPxConverter.dpToPixel(context, bitmapHeight), Bitmap.Config.ARGB_8888);
        invoiceCanvas = new Canvas(invoiceBitmap);
        invoiceCanvas.drawColor(Color.WHITE);

        int x = PADDING;

        // draw image
        drawText(context.getString(R.string.image), x, PADDING);
        x += IMAGE_WIDTH;

        // draw sku
        x += HORIZONTAL_MARGIN;
        drawText(context.getString(R.string.sku), x, PADDING);
        x += SKU_WIDTH;

        // draw description
        x += HORIZONTAL_MARGIN;
        drawText(context.getString(R.string.description), x, PADDING);
        x += DESCRIPTION_WIDTH;

        // draw qty
        x += HORIZONTAL_MARGIN;
        drawText(context.getString(R.string.qty), x, PADDING);
        x += QTY_WIDTH;

        // draw price
        x += HORIZONTAL_MARGIN;
        drawText(context.getString(R.string.price), x, PADDING);

        loadLamp();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackToDesignEvent(BackToDesignEvent event) {

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


    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            drawInvoiceItem(bitmap);
            invoiceIndex++;
            loadLamp();
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            drawInvoiceItem();
            invoiceIndex++;
            loadLamp();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private void loadLamp() {
        if (invoiceIndex < sessionContext.getInvoiceItems().size()) {
            InvoiceItem invoiceItem = sessionContext.getInvoiceItems().get(invoiceIndex);
            if (invoiceItem.getLamp().getSources().get(0).getSourceType() == Source.FILE) {
                picasso.load(new File(invoiceItem.getLamp().getSources().get(0).getSource())).resize(DpPxConverter.dpToPixel(context, IMAGE_WIDTH), DpPxConverter.dpToPixel(context,ITEM_HEIGHT)).centerInside().into(target);
            }
        } else {
            drawTotalData();
            invoiceView.hidePreloader();
            String invoicePath = downloadImage(invoiceBitmap, "estimate");
            sessionContext.setInvoicePath(invoicePath);
            invoiceView.setInvoiceBitmap(invoiceBitmap);
        }
    }

    private void drawInvoiceItem(Bitmap bitmap) {
        int y = (ITEM_HEIGHT + VERTICAL_MARGIN) * (invoiceIndex + 1);

        InvoiceItem invoiceItem = sessionContext.getInvoiceItems().get(invoiceIndex);

        int x = PADDING;

        // draw image
        if (bitmap != null)
            drawImage(bitmap, x, y);
        x += IMAGE_WIDTH;

        // draw sku
        x += HORIZONTAL_MARGIN;
        drawText(invoiceItem.getLamp().getId(), x, y + ITEM_HEIGHT/3);
        x += SKU_WIDTH;

        // draw description
        x += HORIZONTAL_MARGIN;
        drawTotalDescription(invoiceItem.getLamp().getDescription(), x, y + ITEM_HEIGHT/3);
        x += DESCRIPTION_WIDTH;

        // draw qty
        x += HORIZONTAL_MARGIN;
        drawText(String.valueOf(invoiceItem.getQuantity()), x, y + ITEM_HEIGHT/3);
        x += QTY_WIDTH;

        // draw price
        x += HORIZONTAL_MARGIN;
        drawText(currencyFormat.format(invoiceItem.getLamp().getPrice()), x, y + ITEM_HEIGHT/3);
    }

    private void drawInvoiceItem() {
        this.drawInvoiceItem(null);
    }

    private void drawTotalData() {
        int x = PADDING;
        int valueX = x + TOTAL_TEXT_WIDTH + HORIZONTAL_MARGIN;
        int y = (sessionContext.getInvoiceItems().size() + 1) * (ITEM_HEIGHT + VERTICAL_MARGIN) + TOTAL_TEXT_TOP_MARGIN;

        float total = 0f;
        for (InvoiceItem invoiceItem : sessionContext.getInvoiceItems()) {
            total += invoiceItem.getTotalPrice();
        }
        float tax = 0f;
        float oderTotal = 0f;

        // draw total
        drawTotalText(context.getString(R.string.total), x, y);
        drawTotalText(currencyFormat.format(total), valueX, y);

        // draw tax
        y += VERTICAL_MARGIN;
        drawTotalText(context.getString(R.string.tax), x, y);
        drawTotalText(currencyFormat.format(tax), valueX, y);

        // draw order total
        y += VERTICAL_MARGIN;
        drawTotalText(context.getString(R.string.order_total), x, y);
        drawTotalText(currencyFormat.format(oderTotal), valueX, y);
    }

    private void drawText(String text, int x, int y) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(DpPxConverter.dpToPixel(context, 12));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        invoiceCanvas.drawBitmap(invoiceBitmap, 0f, 0f, paint);
        invoiceCanvas.drawText(text, DpPxConverter.dpToPixel(context, x), DpPxConverter.dpToPixel(context, y), paint);
    }

    private void drawTotalText(String text, int x, int y) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(DpPxConverter.dpToPixel(context, 12));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        invoiceCanvas.drawBitmap(invoiceBitmap, 0f, 0f, paint);
        invoiceCanvas.drawText(text, DpPxConverter.dpToPixel(context, x), DpPxConverter.dpToPixel(context, y), paint);
    }

    private void drawTotalDescription(String text, int x, int y) {
        TextPaint paint = new TextPaint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(DpPxConverter.dpToPixel(context, 12));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        StaticLayout.Builder textLayout = StaticLayout.Builder.obtain(text, 0, text.length(), paint, DpPxConverter.dpToPixel(context, DESCRIPTION_WIDTH));
        textLayout.setMaxLines(3);

        invoiceCanvas.save();
        invoiceCanvas.translate(DpPxConverter.dpToPixel(context, x), DpPxConverter.dpToPixel(context, y-20));
        textLayout.build().draw(invoiceCanvas);
        invoiceCanvas.restore();
    }

    private void drawImage(Bitmap bitmap, int x, int y) {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        invoiceCanvas.drawBitmap(bitmap, DpPxConverter.dpToPixel(context, x), DpPxConverter.dpToPixel(context, y), paint);
    }
}
