package com.atproj.zugara.lampsplus.repository;

import com.atproj.zugara.lampsplus.model.Item;

import java.util.List;

public interface LampsplusRepository {

    void getRootChildren(BaseResponse<List<Item>> response);
    void getChildren(Item item, BaseResponse<List<Item>> response);
}
