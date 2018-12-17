package com.zugara.atproj.lampsplus.utils;

import java.util.List;

/**
 * Created by andre on 17-Dec-18.
 */

public interface IFileManager {
    void init();
    void addListener(FileManagerListener listener);
    void removeListener(FileManagerListener listener);
    void goFolder(int position);
    void goBack();
}
