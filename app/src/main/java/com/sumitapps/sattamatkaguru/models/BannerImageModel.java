package com.sumitapps.sattamatkaguru.models;

public class BannerImageModel {
    String id,Image,Title,ImageUrl;

    public BannerImageModel(String id, String image, String title, String imageUrl) {
        this.id = id;
        Image = image;
        Title = title;
        ImageUrl = imageUrl;
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

    public String getImageUrl() {
        return ImageUrl;
    }
}
