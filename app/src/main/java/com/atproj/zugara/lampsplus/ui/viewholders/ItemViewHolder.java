package com.atproj.zugara.lampsplus.ui.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.atproj.zugara.lampsplus.dagger.App;

import butterknife.ButterKnife;

/**
 * Created by andre on 14-Dec-18.
 */

public abstract class ItemViewHolder<Item> extends RecyclerView.ViewHolder {

    protected final Context context;

    public ItemViewHolder(View view) {
        super(view);
        context = view.getContext();
        App.get(context).inject(this);
        ButterKnife.bind(this, view);
    }

    public abstract void setItem(Item item);
}
