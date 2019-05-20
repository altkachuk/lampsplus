package com.atproj.zugara.lampsplus.ui.viewholders;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atproj.zugara.lampsplus.draganddrop.OnDragTouchListener;
import com.atproj.zugara.lampsplus.draganddrop.shadow.ImageDragShadowBuilder;
import com.atproj.zugara.lampsplus.model.Folder;
import com.atproj.zugara.lampsplus.model.Item;
import com.atproj.zugara.lampsplus.model.VisibleItem;
import com.atproj.zugara.lampsplus.utils.SourceLoader;
import com.squareup.picasso.Picasso;
import com.atproj.zugara.lampsplus.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnLongClick;

/**
 * Created by andre on 15-Dec-18.
 */

public class FileViewHolder extends ItemViewHolder<Item> {

    @Inject
    Picasso picasso;

    @BindView(R.id.image_view)
    ImageView imageView;

    @BindView(R.id.folderView)
    ImageView folderView;

    @BindView(R.id.nameText)
    TextView nameText;

    public FileViewHolder(final View view) {
        super(view);

        imageView.setOnTouchListener(new OnDragTouchListener(context) {
            @Override
            public void onInitDrag() {
                startDrag(imageView);
                vibrate();
            }
        });
    }

    @Override
    public void setItem(Item item) {
        if (item instanceof Folder) {
            folderView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            nameText.setText(item.getName());
        } else if (item instanceof VisibleItem) {
            folderView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setTag(item);

            VisibleItem visibleItem = (VisibleItem) item;
            try {
                SourceLoader.loadSource(picasso, visibleItem.getSources().get(0), imageView);
                nameText.setText(visibleItem.getDescription());
            } catch (Exception e) {
                ;
            }
        }

    }

    @OnLongClick(R.id.image_view)
    public boolean onLongClickLampView(View v) {
        startDrag(v);
        vibrate();

        return true;
    }

    private void startDrag(View v) {
        ClipData.Item item = new ClipData.Item(v.getTag().toString());
        ClipData dragData = new ClipData(v.getTag().toString(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

        // Instantiates the drag shadow builder.
        View.DragShadowBuilder shadowBuilder = new ImageDragShadowBuilder(imageView);

        // Starts the drag
        v.startDrag(dragData, shadowBuilder, imageView, 0);
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
