package com.zugara.atproj.lampsplus.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.model.FileItem;
import com.zugara.atproj.lampsplus.ui.viewholders.FileViewHolder;

import java.io.File;
import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public class FileAdapter extends ItemAdapter<FileItem, FileViewHolder> {

    public FileAdapter(Context context, List<FileItem> fileList) {
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
