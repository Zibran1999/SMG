package com.sumitapps.sattamatkaguru.models;

public class TodayResultModel implements Comparable {
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


    @Override
    public int compareTo(Object o) {
        TodayResultModel resultModel = (TodayResultModel) o;
        if (resultModel.getNewNo().equals(this.NewNo))
        return 0;
        return 1;
    }
}
