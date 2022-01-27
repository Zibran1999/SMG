package com.sumitapps.sattamatkaguru.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

public class TodayPageViewModel extends AndroidViewModel {
    private final Repository repository;
    private final Map<String, String> map;

    public TodayPageViewModel(@NonNull Application application, Map<String, String> map) {
        super(application);
        this.map = map;
        repository = new Repository();
    }

    public LiveData<BannerImageModleList> getBannerImageList() {
        return repository.getBannerImagesLiveData(map);
    }
    public LiveData<TodayResultModelList> getTodayResultList() {
        return repository.getTodayResultLiveData(map);
    }
}
