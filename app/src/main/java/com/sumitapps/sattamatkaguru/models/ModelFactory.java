package com.sumitapps.sattamatkaguru.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

public class ModelFactory implements ViewModelProvider.Factory {
    Application application;
    Map<String,String> map;
    public ModelFactory(Application application, Map<String, String> map) {
        this.map = map;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TodayPageViewModel(application,map);
    }
}
