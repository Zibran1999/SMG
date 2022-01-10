package com.sumitapps.sattamatkaguru.models;

public class NewsModel {

    private final String id;
    private final String Image;
    private final String Title;
    private final String Desc;

    public NewsModel(String id, String image, String title, String desc) {
        this.id = id;
        Image = image;
        Title = title;
        Desc = desc;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return Image;
    }

    public String getTitle() {
        return Title;
    }

    public String getDesc() {
        return Desc;
    }
}
