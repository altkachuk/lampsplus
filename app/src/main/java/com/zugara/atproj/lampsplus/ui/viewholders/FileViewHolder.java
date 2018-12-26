package com.zugara.atproj.lampsplus.ui.viewholders;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.drag.shadow.ImageDragShadowBuilder;
import com.zugara.atproj.lampsplus.model.ItemFile;
import com.zugara.atproj.lampsplus.ui.view.DraggableImage;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnLongClick;

/**
 * Created by andre on 15-Dec-18.
 */

public class FileViewHolder extends ItemViewHolder<ItemFile> {

    private static final String IMAGEVIEW_TAG = "icon bitmap";

    @Inject
    Picasso picasso;

    @BindView(R.id.lampView)
    ImageView lampView;

    @BindView(R.id.folderView)
    ImageView folderView;

    @BindView(R.id.nameText)
    TextView nameText;

    private DraggableImage draggableImage;

    public FileViewHolder(View view) {
        super(view);
        lampView.setTag(IMAGEVIEW_TAG);
    }

    @Override
    public void setItem(ItemFile file) {
        if (isFolder(file.getName())) {
            folderView.setVisibility(View.VISIBLE);
            lampView.setVisibility(View.GONE);
        } else {
            folderView.setVisibility(View.GONE);
            lampView.setVisibility(View.VISIBLE);
            draggableImage = new DraggableImage(context);
            draggableImage.setTag(file.getLamp());
            load(file.getSource());
        }
        nameText.setText(file.getName());
    }

    private boolean isFolder(String fileName) {
        return !fileName.contains(".");
    }

    private void load(Object source) {
        if (source instanceof File) {
            picasso.load((File) source).into(lampView);
            picasso.load((File) source).into(draggableImage);
        }
    }

    @OnLongClick(R.id.lampView)
    public boolean onLongClickLampView(View v) {
        // Create a new ClipData.
        // This is done in two steps to provide clarity. The convenience method
        // ClipData.newPlainText() can create a plain text ClipData in one step.

        // Create a new ClipData.Item from the ImageView object's tag
        ClipData.Item item = new ClipData.Item(v.getTag().toString());

        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        ClipData dragData = new ClipData(v.getTag().toString(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

        // Instantiates the drag shadow builder.
        View.DragShadowBuilder shadowBuilder = new ImageDragShadowBuilder(lampView);

        // Starts the drag
        v.startDrag(dragData, shadowBuilder, draggableImage, 0);

        return true;
    }
}
