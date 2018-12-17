package com.zugara.atproj.lampsplus.presenters;

import com.zugara.atproj.lampsplus.model.FileItem;
import com.zugara.atproj.lampsplus.utils.FileManagerListener;
import com.zugara.atproj.lampsplus.utils.FileUtil;
import com.zugara.atproj.lampsplus.utils.IFileManager;
import com.zugara.atproj.lampsplus.views.LampsView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
        fileManager.goFolder(position);
    }
}
