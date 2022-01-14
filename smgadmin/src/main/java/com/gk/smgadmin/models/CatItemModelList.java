package com.gk.smgadmin.models;

import java.util.List;

public class CatItemModelList {
    List<CatItemModel> data = null;

    public CatItemModelList(List<CatItemModel> data) {
        this.data = data;
    }

    public List<CatItemModel> getData() {
        return data;
    }
}
