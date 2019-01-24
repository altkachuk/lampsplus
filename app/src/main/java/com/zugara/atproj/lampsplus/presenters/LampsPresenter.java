package com.zugara.atproj.lampsplus.presenters;

import com.zugara.atproj.lampsplus.fileloader.LocalFileLoader;
import com.zugara.atproj.lampsplus.model.BaseFile;
import com.zugara.atproj.lampsplus.fileloader.FileLoaderListener;
import com.zugara.atproj.lampsplus.fileloader.IFileLoader;
import com.zugara.atproj.lampsplus.model.Folder;
import com.zugara.atproj.lampsplus.views.LampsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public class LampsPresenter {

    private LampsView lampsView;
    private IFileLoader fileLoader;

    private Folder rootFolder;
    private Folder currentFolder;

    public LampsPresenter(LampsView lampsView) {
        this.lampsView = lampsView;
        this.fileLoader = fileLoader;

        fileLoader = new LocalFileLoader();
        fileLoader.addListener(new FileLoaderListener() {
            @Override
            public void onComplete(Folder root) {
                rootFolder = root;
                currentFolder = rootFolder;
                update(currentFolder);
            }

            @Override
            public void onProductListError() {

            }
        });
        fileLoader.load();
    }

    private void update(Folder folder) {
        List<String> breadcrumps = new ArrayList<>();
        BaseFile file = folder;
        while (file.getParent() != null) {
            breadcrumps.add(0, file.getName());
            file = file.getParent();
        }
        breadcrumps.add(0, rootFolder.getName());

        lampsView.enableBackButton(breadcrumps.size() > 1);
        lampsView.setBreadcrumps(breadcrumps);
        lampsView.setDataProvider(folder.getChildren());
    }

    public void back() {
        if (currentFolder.getParent() != null) {
            currentFolder = (Folder) currentFolder.getParent();
            update(currentFolder);
        }
    }

    public void selectFile(int position) {
        Folder folder = (Folder) currentFolder.getChild(position);
        if (folder != null) {
            currentFolder = folder;
            update(currentFolder);
        }
    }
}
