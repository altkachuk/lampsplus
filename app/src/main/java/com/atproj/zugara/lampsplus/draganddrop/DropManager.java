package com.atproj.zugara.lampsplus.draganddrop;

import android.content.ClipDescription;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

public class DropManager implements View.OnDragListener {

    private OnDropListener listener;

    public DropManager(OnDropListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        if (event.getAction() == DragEvent.ACTION_DRAG_STARTED) {
            // Determines if this View can accept the dragged data
            if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                return true;
            }
            return false;
        }

        if (event.getAction() == DragEvent.ACTION_DRAG_ENTERED ||
                event.getAction() == DragEvent.ACTION_DRAG_ENTERED ||
                event.getAction() == DragEvent.ACTION_DRAG_LOCATION ||
                event.getAction() == DragEvent.ACTION_DRAG_EXITED ||
                event.getAction() == DragEvent.ACTION_DRAG_ENDED
        ) {
            return true;
        }

        if (event.getAction() == DragEvent.ACTION_DROP) {
            listener.onDrop(event.getLocalState(), event.getX(), event.getY());
            return true;
        }

        return false;
    }

    public interface OnDropListener {
        void onDrop(Object localState, float dropX, float dropY);
    }
}
