package com.zugara.atproj.lampsplus.filemanager;

import android.util.Log;

import com.zugara.atproj.lampsplus.model.ItemFile;
import com.zugara.atproj.lampsplus.model.Lamp;
import com.zugara.atproj.lampsplus.utils.FileUtils;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by andre on 17-Dec-18.
 */

public class LocalFileManager implements IFileManager {

    private final String TAG = "LocalFileManager";

    private List<File> breadcrumpsList;
    private List<File> fileList;
    private HashMap<String, Lamp> lampList;
    private HashMap<FileManagerListener, FileManagerListener> listenerHashMap = new HashMap<>();

    public LocalFileManager() {

    }

    @Override
    public void init() {
        breadcrumpsList = new ArrayList<>();
        File rootDir = FileUtils.getAppDirectory();
        breadcrumpsList.add(rootDir);
        if (!rootDir.exists()) {
            boolean result = rootDir.mkdirs();
            Log.d(TAG, "root created: " + result);
        }

        lampList = new HashMap<>();
        File excelFile = getExcelFile(breadcrumpsList.get(breadcrumpsList.size()-1));
        readExcelFile(excelFile);
        fileList = getListFiles(breadcrumpsList.get(breadcrumpsList.size()-1));
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
            fileList = getListFiles(breadcrumpsList.get(breadcrumpsList.size()-1));
            update();
        }
    }

    @Override
    public void goBack() {
        if (breadcrumpsList.size() > 1) {
            breadcrumpsList.remove(breadcrumpsList.size()-1);
            fileList = getListFiles(breadcrumpsList.get(breadcrumpsList.size()-1));
            update();
        }
    }

    private File getExcelFile(File parentDir) {
        File[] files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().indexOf(".xls") != -1 || file.getName().indexOf(".xlsx") != -1)
                    return file;
            }
        }

        return null;
    }

    private List<File> getListFiles(File parentDir) {
        List<File> result = new ArrayList<>();

        File[] files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().indexOf(".xls") == -1 && file.getName().indexOf(".xlsx") == -1)
                    result.add(file);
            }
        }

        return result;
    }

    private void update() {
        List<String> breadcrumps = new ArrayList<>();
        for (File file : breadcrumpsList) {
            breadcrumps.add(file.getName());
        }

        List<ItemFile> files = new ArrayList<>();
        for (File file : fileList) {
            String name = file.getName();
            ItemFile item = new ItemFile(name, file);
            if (name.indexOf(".png") != -1) {
                String id = name.substring(0, name.indexOf(".png"));
                if (lampList.containsKey(id)) {
                    item.setLamp(lampList.get(id));
                }
            }
            files.add(item);
        }

        for (FileManagerListener listener : listenerHashMap.keySet()) {
            listener.onUpdate(breadcrumps, files);
        }
    }

    private void readExcelFile(File excelFile) {
        try {
            POIFSFileSystem fileSystem = new POIFSFileSystem(excelFile, true);
            HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
            HSSFSheet sheet = workbook.getSheetAt(0);

            int rowIndex = 0;
            int cellIndex = 0;
            for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator.hasNext(); ) {
                Row row = rowIterator.next();
                if (rowIndex != 0) {
                    String id = "";
                    float price = 0.0f;
                    for (Iterator<Cell> cellIterator = row.cellIterator(); cellIterator.hasNext(); ) {
                        Cell cell = cellIterator.next();
                        if (cellIndex == 0) {
                            id = cell.toString();
                        } else if (cellIndex == 1) {
                            price = Float.parseFloat(cell.toString());
                        }
                        cellIndex++;
                    }
                    Lamp lamp = new Lamp(id, price);
                    lampList.put(id, lamp);
                }
                cellIndex = 0;
                rowIndex++;
            }

            Log.d(TAG, "OK");
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        } catch (NullPointerException e) {
            Log.d(TAG, e.getMessage());
            for (FileManagerListener listener : listenerHashMap.keySet()) {
                listener.onProductListError();
            }
        }

    }

}
