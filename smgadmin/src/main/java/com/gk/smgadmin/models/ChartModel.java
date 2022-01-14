package com.gk.smgadmin.models;

import com.google.gson.annotations.SerializedName;

public class ChartModel {
    String id;
    @SerializedName("ChartName")
    String chartName;
    @SerializedName("ChartUrl")
    String chartUrl;

    public ChartModel(String id, String chartName, String chartUrl) {
        this.id = id;
        this.chartName = chartName;
        this.chartUrl = chartUrl;
    }

    public String getId() {
        return id;
    }

    public String getChartName() {
        return chartName;
    }

    public String getChartUrl() {
        return chartUrl;
    }
}
