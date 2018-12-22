package com.zugara.atproj.lampsplus.dagger.components;

import com.zugara.atproj.lampsplus.dagger.modules.FileManagerModule;
import com.zugara.atproj.lampsplus.filemanager.IFileManager;

import dagger.Component;

/**
 * Created by andre on 17-Dec-18.
 */

@Component(modules = {FileManagerModule.class})
public interface FileManagerComponent {
    IFileManager fileManager();
}
