package com.zugara.atproj.lampsplus.dagger.modules;

import com.zugara.atproj.lampsplus.utils.IFileManager;
import com.zugara.atproj.lampsplus.utils.LocalFileManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by andre on 17-Dec-18.
 */

@Module
public class FileManagerModule {

    @Provides
    IFileManager fileManager() {
        return new LocalFileManager();
    }
}
