package com.atproj.zugara.lampsplus.repository;

import android.util.Log;

import com.atproj.zugara.lampsplus.excel.ExcelFormat;
import com.atproj.zugara.lampsplus.excel.ExcelParser;
import com.atproj.zugara.lampsplus.model.Folder;
import com.atproj.zugara.lampsplus.model.Glow;
import com.atproj.zugara.lampsplus.model.Item;
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

public class LocalLampsplusRepository implements LampsplusRepository {

    private List<Item> allItems;

    public LocalLampsplusRepository(List<Item> allItems) {
        this.allItems = allItems;
    }

    //----------------------------------------------------------------------------------------------
    // LampsplusRepository methods

    @Override
    public void getRootChildren(BaseResponse<List<Item>> response) {
        List<Item> result = new ArrayList<>();
        for (Item item : allItems) {
            if (item.getParent() == null) {
                result.add(item);
            }
        }
        response.onComplete(result);
    }

    @Override
    public void getChildren(Item item, BaseResponse<List<Item>> response) {
        List<Item> result = new ArrayList<>();
        for (Item itm : allItems) {
            if (itm.getParent() != null && itm.getParent().getId().equals(item.getId())) {
                result.add(itm);
            }
        }
        response.onComplete(result);
    }

    // LampsplusRepository methods
    //----------------------------------------------------------------------------------------------

}
