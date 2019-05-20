package com.atproj.zugara.lampsplus.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.atproj.zugara.lampsplus.ui.viewholders.ItemViewHolder;

import java.util.List;

/**
 * Created by andre on 14-Dec-18.
 */

public abstract class ItemAdapter<Item, T extends ItemViewHolder<Item>> extends RecyclerView.Adapter<T> {

    protected final Context context;
    protected List<Item> items;

    public ItemAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull T holder, int position) {
        Item item = items.get(position);
        if (item != null) {
            holder.setItem(item);
        }
    }

    public void setItems(List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
