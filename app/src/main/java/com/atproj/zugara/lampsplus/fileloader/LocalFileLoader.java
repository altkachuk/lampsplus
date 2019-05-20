package com.atproj.zugara.lampsplus.fileloader;

import android.util.Log;

import com.atproj.zugara.lampsplus.excel.ExcelFormat;
import com.atproj.zugara.lampsplus.excel.ExcelParser;
import com.atproj.zugara.lampsplus.model.Folder;
import com.atproj.zugara.lampsplus.model.Glow;
import com.atproj.zugara.lampsplus.model.Lamp;
import com.atproj.zugara.lampsplus.model.Source;
import com.atproj.zugara.lampsplus.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andre on 17-Dec-18.
 */

public class LocalFileLoader implements IFileLoader {

    private final String TAG = "LocalFileLoader";
    private final String[] file_exception = new String[]{
            ".xls",
            "effects",
            "ies_light"};

    private HashMap<String, File> effectList;
    private JSONArray lampList;

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
        root.setName(rootDir.getName());
        parseFiles(root, rootDir);

        for (FileLoaderListener listener : listenerHashMap.keySet()) {
            listener.onComplete(root);
        }
    }

    private void parseConfigFile(File parentDir) {
        lampList = new JSONArray();

        File excelFile = getExcelFile(parentDir);

        ExcelFormat lampFormat = new ExcelFormat(
                "sku",
                "glow_id",
                "glow_x",
                "glow_y",
                "glow_rot",
                "glow_scale",
                "description",
                "price");

        ExcelParser excelParser = new ExcelParser(lampFormat);
        excelParser.setOnExcelParserListener(new ExcelParser.OnExcelParserListener() {
            @Override
            public void onComplete(JSONArray items) {
                lampList = items;
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, e.getMessage());
                for (FileLoaderListener listener : listenerHashMap.keySet()) {
                    listener.onProductListError();
                }
            }
        });
        excelParser.parse(excelFile);
    }

    private void parseEffects(File parentDir) {
        List<File> effects = getListFiles(new File(parentDir, "ies_light"));
        effectList = new HashMap<>();
        for (File effect : effects) {
            String name = effect.getName();
            String id = name.substring(0, name.indexOf(".png"));
            effectList.put(id, effect);
        }
    }

    private void parseFiles(Folder parent, File parentDir) {
        File[] files = parentDir.listFiles();

        if (files != null) {
            for (File file : files) {
                boolean accept = true;
                for (String str : file_exception) {
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
                        Folder folder = new Folder();
                        folder.setName(file.getName());
                        parent.addChild(folder);

                        parseFiles(folder, file);
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

        Source source = new Source();
        source.setSourceType(Source.FILE);
        source.setSource(file.getAbsolutePath());

        List<Source> sources = new ArrayList<>();
        sources.add(source);
        lamp.setSources(sources);

        String id = name.substring(0, name.indexOf(".png"));
        // add data from DB
        for (int i = 0; i < lampList.length(); i++) {
            try {
                JSONObject object = lampList.getJSONObject(i);
                if (object.getString("sku").equals(id)) {
                    lamp.setId(object.getString("sku"));
                    lamp.setDescription(object.getString("description"));
                    lamp.setPrice(object.getLong("price"));

                    String glowId = object.getString("glow_id");
                    if (effectList.containsKey(glowId)) {
                        Glow glow = new Glow();
                        Source glowSource = new Source();
                        glowSource.setSource(effectList.get(glowId).getAbsolutePath());
                        glowSource.setSourceType(Source.FILE);
                        glow.setX(object.getInt("glow_x"));
                        glow.setY(object.getInt("glow_y"));
                        glow.setRotation(object.getInt("glow_rot"));
                        glow.setScale(object.getLong("glow_scale"));
                        List<Glow> glows = new ArrayList<>();
                        glows.add(glow);
                        lamp.setGlows(glows);
                    }

                    break;
                }
            } catch (JSONException e) {
                ;
            }
        }
        return lamp;
    }

}
