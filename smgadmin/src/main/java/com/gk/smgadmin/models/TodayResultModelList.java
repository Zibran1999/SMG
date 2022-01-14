package com.gk.smgadmin.models;

import java.util.List;

public class TodayResultModelList {
    List<TodayResultModel> data = null;

    public TodayResultModelList(List<TodayResultModel> data) {
        this.data = data;
    }

    public List<TodayResultModel> getData() {
        return data;
    }
}
