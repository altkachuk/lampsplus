package com.atproj.zugara.lampsplus.fileloader;

import com.atproj.zugara.lampsplus.model.Folder;

/**
 * Created by andre on 17-Dec-18.
 */

public interface FileLoaderListener {
    void onComplete(Folder root);
    void onProductListError();
}
