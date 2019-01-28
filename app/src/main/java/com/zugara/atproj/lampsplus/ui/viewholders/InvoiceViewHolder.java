package com.zugara.atproj.lampsplus.ui.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.model.InvoiceItem;
import com.zugara.atproj.lampsplus.model.Lamp;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by andre on 25-Jan-19.
 */

public class InvoiceViewHolder extends ItemViewHolder<InvoiceItem> {

    @Inject
    Picasso picasso;

    @BindView(R.id.lampView)
    ImageView lampView;

    @BindView(R.id.skuText)
    TextView skuText;

    @BindView(R.id.descriptionText)
    TextView descriptionText;

    @BindView(R.id.quantityText)
    TextView quantityText;

    @BindView(R.id.priceText)
    TextView priceText;

    public InvoiceViewHolder(View view) {
        super(view);
    }

    @Override
    public void setItem(InvoiceItem item) {
        if (item.getLamp().getSource() instanceof File) {
            picasso.load((File) item.getLamp().getSource()).resize(600, 600).centerCrop().into(lampView);
        }
        skuText.setText(item.getLamp().getId());
        descriptionText.setText(item.getLamp().getDescription());
        quantityText.setText(String.valueOf(item.getQuantity()));
        priceText.setText(String.valueOf(item.getLamp().getPrice()));
    }
}
