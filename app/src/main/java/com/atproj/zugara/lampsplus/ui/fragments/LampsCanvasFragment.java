package com.atproj.zugara.lampsplus.ui.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.atproj.zugara.lampsplus.R;
import com.atproj.zugara.lampsplus.draganddrop.DropManager;
import com.atproj.zugara.lampsplus.model.Glow;
import com.atproj.zugara.lampsplus.model.Lamp;
import com.atproj.zugara.lampsplus.model.Source;
import com.atproj.zugara.lampsplus.presenters.LampsCanvasPresenter;
import com.atproj.zugara.lampsplus.selection.SelectorManager;
import com.atproj.zugara.lampsplus.ui.fragments.base.BaseFragment;
import com.atproj.zugara.lampsplus.ui.view.GlowImage;
import com.atproj.zugara.lampsplus.ui.view.LampImageView;
import com.atproj.zugara.lampsplus.ui.view.SelectImageView;
import com.atproj.zugara.lampsplus.utils.SourceLoader;
import com.atproj.zugara.lampsplus.views.LampsCanvasView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by andre on 15-Dec-18.
 */

public class LampsCanvasFragment extends BaseFragment implements LampsCanvasView {

    @Inject
    Picasso picasso;

    @BindView(R.id.lampsHolder)
    RelativeLayout lampsHolder;

    @BindView(R.id.glowHolder)
    RelativeLayout glowHolder;

    private ProgressDialog progressDialog;
    private LampsCanvasPresenter lampsCanvasPresenter;
    private SelectorManager selectorManager;
    private HashMap<Object, GlowImage> glows = new HashMap<>();

    //-------------------------------------------------------------
    // Lifecycle override

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_canvas, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPreloader();

        selectorManager = new SelectorManager();

        lampsHolder.setOnDragListener(new DropManager(new DropManager.OnDropListener() {
            @Override
            public void onDrop(Object localState, float dropX, float dropY) {
                ImageView imageView = (ImageView) localState;
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                Lamp lamp = (Lamp) imageView.getTag();

                LampImageView lampView = new LampImageView(getContext(), bitmap, lamp);
                lampView.setBound(0,0,lampsHolder.getWidth()-100, lampsHolder.getHeight()-200);

                selectorManager.addItem(lampView);
                lampView.setOnSelectTouchListener(new SelectImageView.OnSelectTouchListener() {
                    @Override
                    public boolean onTouch(MotionEvent event) {
                        selectorManager.onTouch(event);
                        return false;
                    }
                });
                lampsHolder.addView(lampView);

                lampView.move(dropX, dropY);
            }
        }));

        lampsHolder.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                final LampImageView image = (LampImageView) child;
                lampsCanvasPresenter.changeNumOfChildren(lampsHolder.getChildCount());
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                LampImageView image = (LampImageView) child;
                selectorManager.removeItem(image);
                lampsCanvasPresenter.changeNumOfChildren(lampsHolder.getChildCount());
            }
        });

        lampsCanvasPresenter = new LampsCanvasPresenter(getContext(), this);
    }

    //-------------------------------------------------------------
    // LoadingView methods

    @Override
    public void showPreloader() {
        progressDialog.show();
    }

    @Override
    public void hidePreloader() {
        progressDialog.dismiss();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.name_has_illegal_characters), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(int messageResId) {
        showErrorMessage(getString(messageResId));
    }

    //-------------------------------------------------------------
    // CanvasView methods

    @Override
    public void enable() {
        selectorManager.selectTop();
        lampsHolder.setEnabled(true);
        for (int i = 0; i < lampsHolder.getChildCount(); i++) {
            LampImageView image = (LampImageView) lampsHolder.getChildAt(i);
            image.setEnabled(true);
        }
    }

    @Override
    public void disable() {
        selectorManager.deselectAll();
        lampsHolder.setEnabled(false);
        for (int i = 0; i < lampsHolder.getChildCount(); i++) {
            LampImageView image = (LampImageView) lampsHolder.getChildAt(i);
            image.setEnabled(false);
        }
    }

    @Override
    public void mirrorTopItem() {
        ((LampImageView)selectorManager.getTopItem()).mirror();
    }

    @Override
    public List<Lamp> getLamps() {
        List<Lamp> result = new ArrayList<>();
        for (int i = 0; i < lampsHolder.getChildCount(); i++) {
            LampImageView lampView = (LampImageView) lampsHolder.getChildAt(i);
            result.add(lampView.getLamp());
        }

        return result;
    }

    @Override
    public void updateGlows() {
        List<Lamp> lamps = new ArrayList<>();
        List<Matrix> matrices = new ArrayList<>();
        List<Boolean> mirroredList = new ArrayList<>();
        List<Integer> sourceWidthList = new ArrayList<>();
        for (int i = 0; i < lampsHolder.getChildCount(); i++) {
            LampImageView image = (LampImageView) lampsHolder.getChildAt(i);

            lamps.add((Lamp) image.getLamp());
            matrices.add(image.getImageMatrix());
            mirroredList.add(image.isMirrored());
            sourceWidthList.add(((BitmapDrawable)image.getDrawable()).getBitmap().getWidth());
        }


        List<Source> glowSources = new ArrayList<>();
        List<Matrix> glowMatrices = new ArrayList<>();

        clearGlow();

        for (int i = 0; i < lamps.size(); i++) {
            Lamp lamp = lamps.get(i);
            for (int j = 0; j < lamp.getGlows().size(); j++) {
                Glow glow = lamp.getGlows().get(j);
                Matrix matrix = new Matrix();
                float[] values = new float[9];
                matrices.get(i).getValues(values);
                matrix.setValues(values);

                Matrix temp = new Matrix();


                if (mirroredList.get(i))
                    temp.postRotate(-glow.getRotation(), 240, 106);
                else
                    temp.postRotate(glow.getRotation(), 240, 106);

                temp.postScale(glow.getScale(), glow.getScale());

                // move to center
                temp.postTranslate(-240 * glow.getScale(), -106 * glow.getScale());

                // move to right position
                if (mirroredList.get(i))
                    temp.postTranslate(sourceWidthList.get(i) - glow.getX(), glow.getY());
                else
                    temp.postTranslate(glow.getX(), glow.getY());

                matrix.preConcat(temp);

                glowSources.add(glow.getSource());
                glowMatrices.add(matrix);
            }
        }

        for (int i  = 0; i < glowSources.size(); i++) {
            Source source = glowSources.get(i);
            Matrix matrix = glowMatrices.get(i);
            GlowImage glowImage = new GlowImage(getActivity().getApplicationContext());
            glowImage.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            glowHolder.addView(glowImage);

            SourceLoader.loadSource(picasso, source, glowImage);
            glowImage.setImageMatrix(matrix);
        }
    }

    private void clearGlow() {
        glowHolder.setDrawingCacheEnabled(false);
        glowHolder.removeAllViews();
    }

    @Override
    public void copyTopItem() {
        LampImageView lampView = (LampImageView) selectorManager.getTopItem();
        lampsHolder.addView(lampView.clone());
    }

    @Override
    public void removeTopItem() {
        LampImageView lampView = (LampImageView) selectorManager.getTopItem();
        if (lampView != null)
            lampsHolder.removeView(lampView);
    }

    @Override
    public void clear() {
        lampsHolder.removeAllViews();
        glowHolder.removeAllViews();
        glows.clear();
    }

    //-------------------------------------------------------------
    // Private

    private void initPreloader() {
        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }
}
