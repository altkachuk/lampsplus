package com.zugara.atproj.lampsplus.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.model.ItemFile;
import com.zugara.atproj.lampsplus.presenters.LampsPresenter;
import com.zugara.atproj.lampsplus.ui.adapters.FileAdapter;
import com.zugara.atproj.lampsplus.ui.decorator.DividerItemDecoration;
import com.zugara.atproj.lampsplus.ui.fragments.base.BaseFragment;
import com.zugara.atproj.lampsplus.ui.listener.RecyclerItemClickListener;
import com.zugara.atproj.lampsplus.filemanager.IFileManager;
import com.zugara.atproj.lampsplus.views.LampsView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by andre on 15-Dec-18.
 */

public class LampsFragment extends BaseFragment implements LampsView {

    @BindView(R.id.backButton)
    ImageView backButton;

    @BindView(R.id.breadcrumpsText)
    TextView breadcrumpsText;

    @BindView(R.id.fileRecyclerView)
    RecyclerView fileRecyclerView;

    private LampsPresenter lampsPresenter;

    private FileAdapter fileAdapter;

    //-------------------------------------------------------------
    // Lifecycle override

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lamps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fileAdapter = new FileAdapter(getActivity().getApplicationContext(), new ArrayList<ItemFile>());
        initRecyclerView();
        lampsPresenter = new LampsPresenter(this);
    }

    //-------------------------------------------------------------
    // Handlers

    @OnClick(R.id.backButton)
    public void onClickBackButton() {
        lampsPresenter.back();
    }

    //-------------------------------------------------------------
    // LampsView methods

    @Override
    public void setDataProvider(List<ItemFile> fileList) {
        fileAdapter.setItems(fileList);
    }

    @Override
    public void setBreadcrumps(List<String> breadcrumps) {
        String breadcrumpsStr = "";
        for (String item : breadcrumps) {
            if (breadcrumpsStr.length() > 0) {
                breadcrumpsStr += " -> ";
            }
            breadcrumpsStr += item;
        }
        breadcrumpsText.setText(breadcrumpsStr);
    }

    @Override
    public void enableBackButton(boolean enable) {
        backButton.setEnabled(enable);
    }

    //-------------------------------------------------------------
    // Private methods

    private void initRecyclerView() {
        // optimization
        fileRecyclerView.setHasFixedSize(true);
        // set layout
        fileRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        fileRecyclerView.setAdapter(fileAdapter);

        fileRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), R.drawable.divider_file));

        fileRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                lampsPresenter.selectFile(position);
            }
        }));
    }
}
