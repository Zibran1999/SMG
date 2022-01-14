package com.gk.smgadmin.models;

public class CatItemModel {
    String id,CatName,CatUrl;

    public CatItemModel(String id, String catName, String catUrl) {
        this.id = id;
        CatName = catName;
        CatUrl = catUrl;
    }

    public String getId() {
        return id;
    }

    public String getCatName() {
        return CatName;
    }

    public String getCatUrl() {
        return CatUrl;
    }
}
