package com.zugara.atproj.lampsplus.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.model.InvoiceItem;
import com.zugara.atproj.lampsplus.ui.viewholders.InvoiceViewHolder;

import java.util.List;

/**
 * Created by andre on 25-Jan-19.
 */

public class InvoiceAdapter extends ItemAdapter<InvoiceItem, InvoiceViewHolder> {

    public InvoiceAdapter(Context context, List<InvoiceItem> items) {
        super(context);
        this.items = items;
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_invoice, parent, false);
        return new InvoiceViewHolder(view);
    }
}
