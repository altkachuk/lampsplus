package com.atproj.zugara.lampsplus.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atproj.zugara.lampsplus.model.Item;
import com.atproj.zugara.lampsplus.R;
import com.atproj.zugara.lampsplus.ui.viewholders.FileViewHolder;

import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public class FileAdapter extends ItemAdapter<Item, FileViewHolder> {

    public FileAdapter(Context context, List<Item> fileList) {
        super(context);
        items = fileList;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_file, parent, false);
        return new FileViewHolder(view);
    }
}
