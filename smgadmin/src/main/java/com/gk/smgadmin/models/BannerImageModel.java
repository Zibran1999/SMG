package com.gk.smgadmin.models;

public class BannerImageModel {
    String id,CatItemId,Image,Title,ImageUrl;

    public BannerImageModel(String id, String catItemId, String image, String title, String imageUrl) {
        this.id = id;
        CatItemId = catItemId;
        Image = image;
        Title = title;
        ImageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getCatItemId() {
        return CatItemId;
    }

    public String getImage() {
        return Image;
    }

    public String getTitle() {
        return Title;
    }

    public String getImageUrl() {
        return ImageUrl;
    }
}
