package com.sumitapps.sattamatkaguru.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static ApiInterface apiInterface;
    private static Repository repository;
    private final MutableLiveData<NewsModelList> newsModelLiveData = new MutableLiveData<>();
    private final MutableLiveData<CatItemModelList> catItemLiveData = new MutableLiveData<>();
    private final MutableLiveData<ChartItemModelList> chartItemLiveData = new MutableLiveData<>();


    public Repository() {
        apiInterface = ApiWebServices.getApiInterface();
    }

    public static Repository getInstance() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    public LiveData<NewsModelList> getNewsLiveData() {
        Call<NewsModelList> call = apiInterface.getAllNews();
        call.enqueue(new Callback<NewsModelList>() {
            @Override
            public void onResponse(@NonNull Call<NewsModelList> call, @NonNull Response<NewsModelList> response) {
                if (response.isSuccessful()) {
                    newsModelLiveData.setValue(response.body());
                } else {
                    Log.d("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsModelList> call, @NonNull Throwable t) {
                Log.d("onResponse Error", t.getMessage());
            }
        });
        return newsModelLiveData;
    }

    public LiveData<CatItemModelList> getCatItemLiveData() {
        Call<CatItemModelList> call = apiInterface.getAllCatItems();
        call.enqueue(new Callback<CatItemModelList>() {
            @Override
            public void onResponse(Call<CatItemModelList> call, Response<CatItemModelList> response) {
                if (response.isSuccessful()) {
                    catItemLiveData.setValue(response.body());
                } else {
                    Log.d("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<CatItemModelList> call, Throwable t) {
                Log.d("onResponse error", t.getMessage());
            }
        });
        return catItemLiveData;
    }

    public LiveData<ChartItemModelList> getChartItemLiveData() {
        Call<ChartItemModelList> call = apiInterface.getAllCharts();
        call.enqueue(new Callback<ChartItemModelList>() {
            @Override
            public void onResponse(@NonNull Call<ChartItemModelList> call, @NonNull Response<ChartItemModelList> response) {
                if (response.isSuccessful()) {
                    chartItemLiveData.setValue(response.body());
                } else {
                    Log.d("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChartItemModelList> call, @NonNull Throwable t) {
                Log.d("onResponse error", t.getMessage());
            }
        });
        return chartItemLiveData;
    }
}
