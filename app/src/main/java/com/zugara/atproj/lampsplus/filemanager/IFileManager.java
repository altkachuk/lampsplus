package com.zugara.atproj.lampsplus.filemanager;

import com.zugara.atproj.lampsplus.filemanager.FileManagerListener;

/**
 * Created by andre on 17-Dec-18.
 */

public interface IFileManager {
    void init();
    void addListener(FileManagerListener listener);
    void removeListener(FileManagerListener listener);
    void goInside(int position);
    void goBack();
}
