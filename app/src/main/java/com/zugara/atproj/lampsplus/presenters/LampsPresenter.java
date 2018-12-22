package com.zugara.atproj.lampsplus.presenters;

import com.zugara.atproj.lampsplus.model.FileItem;
import com.zugara.atproj.lampsplus.filemanager.FileManagerListener;
import com.zugara.atproj.lampsplus.filemanager.IFileManager;
import com.zugara.atproj.lampsplus.views.LampsView;

import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public class LampsPresenter {

    private LampsView lampsView;
    private IFileManager fileManager;

    public LampsPresenter(LampsView lampsView, IFileManager fileManager) {
        this.lampsView = lampsView;
        this.fileManager = fileManager;

        init();
    }

    private void init() {
        fileManager.addListener(new FileManagerListener() {
            @Override
            public void onUpdate(List<String> breadcrumps, List<FileItem> fileList) {
                lampsView.enableBackButton(breadcrumps.size() > 1);
                lampsView.setBreadcrumps(breadcrumps);
                lampsView.setDataProvider(fileList);
            }
        });

        fileManager.init();
    }

    public void back() {
        fileManager.goBack();
    }

    public void selectFile(int position) {
        fileManager.goInside(position);
    }
}
