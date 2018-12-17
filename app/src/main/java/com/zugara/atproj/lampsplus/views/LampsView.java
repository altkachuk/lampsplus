package com.zugara.atproj.lampsplus.views;

import com.zugara.atproj.lampsplus.model.FileItem;

import java.io.File;
import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public interface LampsView {
    void setDataProvider(List<FileItem> fileList);
    void setBreadcrumps(List<String> breadcrumps);
    void enableBackButton(boolean enable);
}
