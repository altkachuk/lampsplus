package com.zugara.atproj.lampsplus.ui.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.model.FileItem;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by andre on 15-Dec-18.
 */

public class FileViewHolder extends ItemViewHolder<FileItem> {

    @Inject
    Picasso picasso;

    @BindView(R.id.lampView)
    ImageView lampView;

    @BindView(R.id.folderView)
    ImageView folderView;

    @BindView(R.id.nameText)
    TextView nameText;

    public FileViewHolder(View view) {
        super(view);
    }

    @Override
    public void setItem(FileItem file) {
        if (isFolder(file.getName())) {
            lampView.setVisibility(View.GONE);
            folderView.setVisibility(View.VISIBLE);
        } else {
            lampView.setVisibility(View.VISIBLE);
            folderView.setVisibility(View.GONE);
            load(file.getSource());
        }

        nameText.setText(file.getName());
    }

    private void load(Object source) {
        if (source instanceof File) {
            picasso.load((File) source).into(lampView);
        }
    }

    private boolean isFolder(String fileName) {
        return !fileName.contains(".");
    }
}
