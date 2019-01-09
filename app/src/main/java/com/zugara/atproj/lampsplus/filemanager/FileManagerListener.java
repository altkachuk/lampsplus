package com.zugara.atproj.lampsplus.filemanager;

import com.zugara.atproj.lampsplus.model.ItemFile;

import java.util.List;

/**
 * Created by andre on 17-Dec-18.
 */

public interface FileManagerListener {
    void onUpdate(List<String> breadcrumps, List<ItemFile> lampList);
    void onProductListError();
}
