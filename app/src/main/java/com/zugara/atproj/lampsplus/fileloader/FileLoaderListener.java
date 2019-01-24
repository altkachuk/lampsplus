package com.zugara.atproj.lampsplus.fileloader;

import com.zugara.atproj.lampsplus.model.Folder;

/**
 * Created by andre on 17-Dec-18.
 */

public interface FileLoaderListener {
    void onComplete(Folder root);
    void onProductListError();
}
