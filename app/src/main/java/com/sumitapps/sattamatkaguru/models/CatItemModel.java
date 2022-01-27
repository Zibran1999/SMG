package com.sumitapps.sattamatkaguru.models;

public class CatItemModel {
    String id,CatName;

    public CatItemModel(String id, String catName) {
        this.id = id;
        CatName = catName;
    }

    public String getId() {
        return id;
    }

    public String getCatName() {
        return CatName;
    }
}
