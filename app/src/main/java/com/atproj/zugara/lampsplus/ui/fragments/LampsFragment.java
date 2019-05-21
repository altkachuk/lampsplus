package com.atproj.zugara.lampsplus.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atproj.zugara.lampsplus.model.Item;
import com.atproj.zugara.lampsplus.R;
import com.atproj.zugara.lampsplus.presenters.LampsPresenter;
import com.atproj.zugara.lampsplus.ui.adapters.FileAdapter;
import com.atproj.zugara.lampsplus.ui.decorator.DividerItemDecoration;
import com.atproj.zugara.lampsplus.ui.fragments.base.BaseFragment;
import com.atproj.zugara.lampsplus.ui.listener.RecyclerItemClickListener;
import com.atproj.zugara.lampsplus.views.LampsView;

import java.util.ArrayList;
import java.util.List;

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
        fileAdapter = new FileAdapter(getActivity().getApplicationContext(), new ArrayList<Item>());
        initRecyclerView();
        lampsPresenter = new LampsPresenter(getActivity().getApplicationContext(), this);
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
    public void setDataProvider(List<Item> fileList) {
        fileAdapter.setItems(fileList);
    }

    @Override
    public void setBreadcrumps(List<String> breadcrumpsList) {
        String breadcrumps = null;
        if (breadcrumpsList.size() > 0) {
            breadcrumps = breadcrumpsList.get(0);
            for (int i = 1; i < breadcrumpsList.size(); i++) {
                breadcrumps += " -> " + breadcrumpsList.get(i);
            }
        }
        breadcrumpsText.setText(breadcrumps);
        backButton.setEnabled(breadcrumpsList.size() > 1);
    }

    @Override
    public void showProductListError() {
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.product_list_error), Toast.LENGTH_SHORT).show();
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
                lampsPresenter.selectItem(position);
            }
        }));
    }
}
