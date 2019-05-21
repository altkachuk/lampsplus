package com.atproj.zugara.lampsplus.presenters;

import android.content.Context;

import com.atproj.zugara.lampsplus.repository.BaseResponse;
import com.atproj.zugara.lampsplus.repository.LampsplusRepository;
import com.atproj.zugara.lampsplus.model.Folder;
import com.atproj.zugara.lampsplus.model.Item;
import com.atproj.zugara.lampsplus.views.LampsView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by andre on 15-Dec-18.
 */

public class LampsPresenter extends BasePresenter {

    @Inject
    LampsplusRepository repository;

    private LampsView lampsView;
    private Item currentParent;
    private List<Item> currentItems;

    public LampsPresenter(Context context, LampsView lampsView) {
        super(context);

        this.lampsView = lampsView;


        repository.getRootChildren(new BaseResponse<List<Item>>() {
            @Override
            public void onComplete(List<Item> items) {
                currentParent = null;
                currentItems = items;
                updateItems();
                updateBreadcrumps();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void updateItems() {
        lampsView.setDataProvider(currentItems);
    }

    private void updateBreadcrumps() {
        List<String> breadcrumps = new ArrayList<>();
        if (currentParent != null) {
            breadcrumps.add(currentParent.getName());
            Item item = currentParent;
            while (item.getParent() != null) {
                breadcrumps.add(0, item.getName());
                item = item.getParent();
            }
        }

        breadcrumps.add(0, "BESTLIGHT4U");
        lampsView.setBreadcrumps(breadcrumps);
    }

    public void back() {
        if (currentParent == null) {
            return;
        }
        if (currentParent.getParent() == null) {
            repository.getRootChildren(new BaseResponse<List<Item>>() {
                @Override
                public void onComplete(List<Item> items) {
                    currentParent = null;
                    currentItems = items;
                    updateItems();
                    updateBreadcrumps();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        } else  {
            repository.getChildren(currentParent.getParent(), new BaseResponse<List<Item>>() {
                @Override
                public void onComplete(List<Item> items) {
                    currentParent = currentParent.getParent();
                    currentItems = items;
                    updateItems();
                    updateBreadcrumps();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }

    public void selectItem(int position) {
        final Item selectedItem = currentItems.get(position);
        if (!(selectedItem instanceof Folder)) {
            return;
        }

        repository.getChildren(currentItems.get(position), new BaseResponse<List<Item>>() {
            @Override
            public void onComplete(List<Item> items) {
                currentParent = selectedItem;
                currentItems = items;
                updateItems();
                updateBreadcrumps();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
