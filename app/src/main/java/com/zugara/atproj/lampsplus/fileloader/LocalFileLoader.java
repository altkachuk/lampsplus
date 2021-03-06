package com.zugara.atproj.lampsplus.fileloader;

import android.util.Log;

import com.zugara.atproj.lampsplus.model.BaseFile;
import com.zugara.atproj.lampsplus.model.Folder;
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

public class LocalFileLoader implements IFileLoader {

    private final String TAG = "LocalFileLoader";
    private final List<String> file_exception = new ArrayList<String>(){{add(".xls");add("effects");}};

    private HashMap<String, File> effectList;
    private HashMap<String, Lamp> lampList;

    private HashMap<FileLoaderListener, FileLoaderListener> listenerHashMap = new HashMap<>();

    public LocalFileLoader() {

    }

    @Override
    public void load() {
        File rootDir = FileUtils.getAppDirectory();

        if (!rootDir.exists()) {
            boolean result = rootDir.mkdirs();
            Log.d(TAG, "root created: " + result);
            for (FileLoaderListener listener : listenerHashMap.keySet()) {
                listener.onProductListError();
            }
            return;
        }

        parseConfigFile(rootDir);
        parseEffects(rootDir);

        Folder root = new Folder();
        root.setSource(rootDir);
        root.setName(rootDir.getName());
        parseFiles(root, file_exception);

        for (FileLoaderListener listener : listenerHashMap.keySet()) {
            listener.onComplete(root);
        }
    }

    private void parseConfigFile(File parentDir) {
        lampList = new HashMap<>();
        File excelFile = getExcelFile(parentDir);
        readExcelFile(excelFile);
    }

    private void parseEffects(File parentDir) {
        List<File> effects = getListFiles(new File(parentDir, "effects"));
        effectList = new HashMap<>();
        for (File effect : effects) {
            String name = effect.getName();
            String id = name.substring(0, name.indexOf(".png"));
            effectList.put(id, effect);
        }
    }

    private void parseFiles(Folder parent, List<String> listException) {
        File[] files = ((File)parent.getSource()).listFiles();

        if (files != null) {
            for (File file : files) {
                boolean accept = true;
                for (String str : listException) {
                    if (file.getName().indexOf(str) != -1) {
                        accept = false;
                    }
                }
                if (accept) {
                    String name = file.getName();
                    if (name.indexOf(".png") != -1) {
                        Lamp lamp = createLamp(file);
                        parent.addChild(lamp);
                    } else {
                        Folder folder = createFolder(file);
                        parent.addChild(folder);

                        parseFiles(folder, listException);
                    }
                }
            }
        }
    }

    @Override
    public void addListener(FileLoaderListener listener) {
        if (listenerHashMap.get(listener) == null) {
            listenerHashMap.put(listener, listener);
        }
    }

    @Override
    public void removeListener(FileLoaderListener listener) {
        if (listenerHashMap.get(listener) != null) {
            listenerHashMap.remove(listener);
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

    private List<File> getListFiles(File parentDir, List<String> listException) {
        List<File> result = new ArrayList<>();

        File[] files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                boolean accept = true;
                for (String str : listException) {
                    if (file.getName().indexOf(str) != -1) {
                        accept = false;
                    }
                }
                if (accept) result.add(file);
            }
        }

        return result;
    }

    private List<File> getListFiles(File parentDir) {
        return this.getListFiles(parentDir, new ArrayList<String>());
    }

    private Lamp createLamp(File file) {
        String name = file.getName();
        Lamp lamp = new Lamp();
        lamp.setName(name);
        lamp.setSource(file);

        String id = name.substring(0, name.indexOf(".png"));
        // add data from DB
        if (lampList.containsKey(id)) {
            Lamp configLamp = lampList.get(id);
            lamp.setId(configLamp.getId());
            lamp.setDescription(configLamp.getDescription());
            lamp.setPrice(configLamp.getPrice());
        }
        // add glow
        if (effectList.containsKey(id)) {
            BaseFile glow = new BaseFile();
            glow.setName("glow");
            glow.setSource(effectList.get(id));
            lamp.setGlow(glow);
        }
        return lamp;
    }

    private Folder createFolder(File file) {
        Folder folder = new Folder();
        folder.setName(file.getName());
        folder.setSource(file);
        return folder;
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
                    String description = "";
                    float price = 0.0f;
                    for (Iterator<Cell> cellIterator = row.cellIterator(); cellIterator.hasNext(); ) {
                        Cell cell = cellIterator.next();
                        if (cellIndex == 0) {
                            id = cell.toString();
                        } else if (cellIndex == 1) {
                            description = cell.toString();
                        } else if (cellIndex == 2) {
                            price = Float.parseFloat(cell.toString());
                        }
                        cellIndex++;
                    }
                    Lamp lamp = new Lamp();
                    lamp.setId(id);
                    lamp.setDescription(description);
                    lamp.setPrice(price);
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
            for (FileLoaderListener listener : listenerHashMap.keySet()) {
                listener.onProductListError();
            }
        }

    }

}
