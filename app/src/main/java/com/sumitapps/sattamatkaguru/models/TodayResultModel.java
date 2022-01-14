package com.sumitapps.sattamatkaguru.models;

public class TodayResultModel {
    String id,ResName,ResultTime,NewNo;

    public TodayResultModel(String id, String resName, String resultTime, String newNo) {
        this.id = id;
        ResName = resName;
        ResultTime = resultTime;
        NewNo = newNo;
    }

    public String getId() {
        return id;
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
