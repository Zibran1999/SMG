package com.sumitapps.sattamatkaguru.utils;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.sumitapps.sattamatkaguru.models.TodayResultModel;

import java.util.ArrayList;
import java.util.List;

public class MyDillUtilCallBack extends DiffUtil.Callback {

    List<TodayResultModel> oldList = new ArrayList<>();
    List<TodayResultModel> newList = new ArrayList<>();

    public MyDillUtilCallBack(List<TodayResultModel> oldList, List<TodayResultModel> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;

    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newList.get(newItemPosition).compareTo(oldList.get(oldItemPosition));
        if (result==0)
            return true;
        return false;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        TodayResultModel newModel  =newList.get(newItemPosition);
        TodayResultModel oldModel  =oldList.get(oldItemPosition);
        Bundle bundle = new Bundle();

        if (!newModel.getNewNo().equals(oldModel.getNewNo())){
            bundle.putString("NewNo",newModel.getNewNo());
        }

        if (bundle.size()==0){
            return null;
        }
        return bundle;

    }
}
