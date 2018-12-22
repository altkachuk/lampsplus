package com.zugara.atproj.lampsplus.filemanager;

import android.util.Log;

import com.zugara.atproj.lampsplus.filemanager.FileManagerListener;
import com.zugara.atproj.lampsplus.filemanager.IFileManager;
import com.zugara.atproj.lampsplus.model.FileItem;
import com.zugara.atproj.lampsplus.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andre on 17-Dec-18.
 */

public class LocalFileManager implements IFileManager {

    private final String TAG = "LocalFileManager";

    private List<File> breadcrumpsList;
    private List<File> fileList;
    private HashMap<FileManagerListener, FileManagerListener> listenerHashMap = new HashMap<>();

    public LocalFileManager() {

    }

    @Override
    public void init() {
        breadcrumpsList = new ArrayList<>();
        File rootDir = FileUtil.getAppDirectory();
        breadcrumpsList.add(rootDir);
        if (!rootDir.exists()) {
            boolean result = rootDir.mkdirs();
            Log.d(TAG, "root created: " + result);
        }
        fileList = FileUtil.getListFiles(breadcrumpsList.get(breadcrumpsList.size()-1));
        update();
    }

    @Override
    public void addListener(FileManagerListener listener) {
        if (listenerHashMap.get(listener) == null) {
            listenerHashMap.put(listener, listener);
        }
    }

    @Override
    public void removeListener(FileManagerListener listener) {
        if (listenerHashMap.get(listener) != null) {
            listenerHashMap.remove(listener);
        }
    }

    @Override
    public void goInside(int position) {
        File file = fileList.get(position);
        if (file.isDirectory()) {
            breadcrumpsList.add(file);
            fileList = FileUtil.getListFiles(breadcrumpsList.get(breadcrumpsList.size()-1));
            update();
        }
    }

    @Override
    public void goBack() {
        if (breadcrumpsList.size() > 1) {
            breadcrumpsList.remove(breadcrumpsList.size()-1);
            fileList = FileUtil.getListFiles(breadcrumpsList.get(breadcrumpsList.size()-1));
            update();
        }
    }

    private void update() {
        List<String> breadcrumps = new ArrayList<>();
        for (File file : breadcrumpsList) {
            breadcrumps.add(file.getName());
        }

        List<FileItem> files = new ArrayList<>();
        for (File file : fileList) {
            files.add(new FileItem(file.getName(), file));
        }

        for (FileManagerListener listener : listenerHashMap.keySet()) {
            listener.onUpdate(breadcrumps, files);
        }
    }

}
