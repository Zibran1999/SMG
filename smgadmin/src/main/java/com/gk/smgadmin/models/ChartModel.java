package com.gk.smgadmin.models;

import com.google.gson.annotations.SerializedName;

public class ChartModel {
    String id;
    @SerializedName("ChartName")
    String chartName;

    public ChartModel(String id, String chartName) {
        this.id = id;
        this.chartName = chartName;
    }

    public String getId() {
        return id;
    }

    public String getChartName() {
        return chartName;
    }
}
