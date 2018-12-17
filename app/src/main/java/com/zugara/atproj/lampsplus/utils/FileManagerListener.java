package com.zugara.atproj.lampsplus.utils;

import com.zugara.atproj.lampsplus.model.FileItem;

import java.util.List;

/**
 * Created by andre on 17-Dec-18.
 */

public interface FileManagerListener {
    void onUpdate(List<String> breadcrumps, List<FileItem> fileList);
}
