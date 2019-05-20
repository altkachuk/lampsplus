package com.atproj.zugara.lampsplus.views;

import com.atproj.zugara.lampsplus.model.Item;

import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public interface LampsView {
    void setDataProvider(List<Item> fileList);
    void setBreadcrumps(List<String> breadcrumps);
    void enableBackButton(boolean enable);
    void showProductListError();
}
