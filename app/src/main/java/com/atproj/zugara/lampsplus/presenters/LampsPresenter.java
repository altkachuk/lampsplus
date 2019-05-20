package com.atproj.zugara.lampsplus.presenters;

import android.content.Context;

import com.atproj.zugara.lampsplus.fileloader.LocalFileLoader;
import com.atproj.zugara.lampsplus.fileloader.FileLoaderListener;
import com.atproj.zugara.lampsplus.fileloader.IFileLoader;
import com.atproj.zugara.lampsplus.model.Folder;
import com.atproj.zugara.lampsplus.model.Item;
import com.atproj.zugara.lampsplus.views.LampsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 15-Dec-18.
 */

public class LampsPresenter extends BasePresenter {

    private LampsView lampsView;
    private IFileLoader fileLoader;

    private Folder rootFolder;
    private Folder currentFolder;

    public LampsPresenter(Context context, LampsView lampsView) {
        super(context);

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
        Item item = folder;
        while (item.getParent() != null) {
            breadcrumps.add(0, item.getName());
            item = item.getParent();
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
        if (!(currentFolder.getChildAt(position) instanceof Folder)) return;

        Folder folder = (Folder) currentFolder.getChildAt(position);
        currentFolder = folder;
        update(currentFolder);
    }
}
