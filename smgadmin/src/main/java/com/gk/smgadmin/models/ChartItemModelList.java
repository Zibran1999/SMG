package com.gk.smgadmin.models;

import java.util.List;

public class ChartItemModelList {
    List<ChartModel> data = null;

    public ChartItemModelList(List<ChartModel> data) {
        this.data = data;
    }

    public List<ChartModel> getData() {
        return data;
    }
}
