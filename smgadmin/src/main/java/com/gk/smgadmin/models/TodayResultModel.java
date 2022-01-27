package com.gk.smgadmin.models;

public class TodayResultModel {
    String id,CatItemId,ResName,ResultTime,NewNo;

    public TodayResultModel(String id, String catItemId, String resName, String resultTime, String newNo) {
        this.id = id;
        CatItemId = catItemId;
        ResName = resName;
        ResultTime = resultTime;
        NewNo = newNo;
    }

    public String getId() {
        return id;
    }

    public String getCatItemId() {
        return CatItemId;
    }

    public String getResName() {
        return ResName;
    }

    public String getResultTime() {
        return ResultTime;
    }

    public String getNewNo() {
        return NewNo;
    }
}
