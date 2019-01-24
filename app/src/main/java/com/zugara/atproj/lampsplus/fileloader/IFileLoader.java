package com.zugara.atproj.lampsplus.fileloader;

/**
 * Created by andre on 17-Dec-18.
 */

public interface IFileLoader {
    void load();
    void addListener(FileLoaderListener listener);
    void removeListener(FileLoaderListener listener);
}
