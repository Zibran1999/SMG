package com.sumitapps.sattamatkaguru.models;

import java.util.List;

public class NewsModelList {
    List<NewsModel> data = null;

    public NewsModelList(List<NewsModel> data) {
        this.data = data;
    }

    public List<NewsModel> getData() {
        return data;
    }
}
