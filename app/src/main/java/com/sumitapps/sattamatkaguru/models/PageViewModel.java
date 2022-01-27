package com.sumitapps.sattamatkaguru.models;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Map;

public class PageViewModel extends AndroidViewModel {
    private final Repository repository;
    private Map<String, String> map;


    public PageViewModel(@NonNull Application application, Map<String, String> map) {
        super(application);
        this.map = map;
        repository = Repository.getInstance();
        Log.d("mapId",map.get("catId"));
    }
    public PageViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();

    }

    public LiveData<NewsModelList> getNews() {
        return repository.getNewsLiveData();
    }

    public LiveData<CatItemModelList> getCatItemList() {
        return repository.getCatItemLiveData();
    }

    public LiveData<ChartItemModelList> getChartItemList() {
        return repository.getChartItemLiveData();
    }





}
