package com.gk.smgadmin.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

public class PageViewModel extends AndroidViewModel {
    private final Repository repository;
    Map<String, String> map;


    public PageViewModel(@NonNull Application application, Map<String, String> map) {
        super(application);
        this.map = map;
        repository = Repository.getInstance();
    }

    public PageViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();

    }

    public LiveData<NewsModelList> getNews() {
        return repository.getNewsLiveData();
    }

    public LiveData<CatItemModelList> getCatItemList(){
        return repository.getCatItemLiveData();
    }

    public LiveData<ChartItemModelList> getChartItemList(){
        return repository.getChartItemLiveData();
    }

    public LiveData<TodayResultModelList> getTodayResultList(){
        return repository.getTodayResultLiveData();
    }

    public LiveData<BannerImageModleList> getBannerImageList(){
        return repository.getBannerImagesLiveData();
    }

}
