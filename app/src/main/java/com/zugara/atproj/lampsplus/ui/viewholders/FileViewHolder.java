package com.zugara.atproj.lampsplus.ui.viewholders;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.drag.OnDragTouchListener;
import com.zugara.atproj.lampsplus.drag.shadow.ImageDragShadowBuilder;
import com.zugara.atproj.lampsplus.model.BaseFile;
import com.zugara.atproj.lampsplus.model.Folder;
import com.zugara.atproj.lampsplus.model.Lamp;
import com.zugara.atproj.lampsplus.ui.view.DraggableImage;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnLongClick;

/**
 * Created by andre on 15-Dec-18.
 */

public class FileViewHolder extends ItemViewHolder<BaseFile> {

    private static final String IMAGEVIEW_TAG = "icon bitmap";

    @Inject
    Picasso picasso;

    @BindView(R.id.lampView)
    ImageView lampView;

    @BindView(R.id.folderView)
    ImageView folderView;

    @BindView(R.id.nameText)
    TextView nameText;

    private View holderView;
    private DraggableImage draggableImage;

    public FileViewHolder(final View view) {
        super(view);
        holderView = view;
        lampView.setTag(IMAGEVIEW_TAG);

        lampView.setOnTouchListener(new OnDragTouchListener(context) {
            @Override
            public void onInitDrag() {
                startDrag(lampView);
                vibrate();
            }
        });
    }

    @Override
    public void setItem(BaseFile file) {
        if (file instanceof Folder) {
            folderView.setVisibility(View.VISIBLE);
            lampView.setVisibility(View.GONE);
            nameText.setText(file.getName());
        } else {
            folderView.setVisibility(View.GONE);
            lampView.setVisibility(View.VISIBLE);
            draggableImage = new DraggableImage(context);
            draggableImage.setTag(file);
            load(file.getSource());
            try {
                nameText.setText(((Lamp)file).getDescription());
            } catch (Exception e) {};
        }

    }

    private void load(Object source) {
        if (source instanceof File) {
            picasso.load((File) source).into(lampView);
            picasso.load((File) source).into(draggableImage);
        }
    }

    @OnLongClick(R.id.lampView)
    public boolean onLongClickLampView(View v) {
        startDrag(v);
        vibrate();

        return true;
    }

    private void startDrag(View v) {
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
    }

    private void vibrate() {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(100);
        }
    }
}
