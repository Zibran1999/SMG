package com.sumitapps.sattamatkaguru.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static ApiInterface apiInterface;
    private static Repository repository;
    private final MutableLiveData<NewsModelList> newsModelLiveData = new MutableLiveData<>();
    private final MutableLiveData<CatItemModelList> catItemLiveData = new MutableLiveData<>();
    private final MutableLiveData<ChartItemModelList> chartItemLiveData = new MutableLiveData<>();
    private final MutableLiveData<TodayResultModelList> todayResultLiveData = new MutableLiveData<>();
    private final MutableLiveData<BannerImageModleList> bannerImageLiveData = new MutableLiveData<>();


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

    public LiveData<TodayResultModelList> getTodayResultLiveData(Map<String,String> map){
        Call<TodayResultModelList> call = apiInterface.getAllTodayResult(map);
        call.enqueue(new Callback<TodayResultModelList>() {
            @Override
            public void onResponse(@NonNull Call<TodayResultModelList> call, @NonNull Response<TodayResultModelList> response) {
                if (response.isSuccessful()) {
                    todayResultLiveData.setValue(response.body());
                } else {
                    Log.d("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TodayResultModelList> call, @NonNull Throwable t) {
                Log.d("onResponse error", t.getMessage());
            }
        });
        return todayResultLiveData;
    }


    public LiveData<BannerImageModleList> getBannerImagesLiveData( Map<String, String> map){

        Call<BannerImageModleList> call = apiInterface.getAllBannerImages(map);
        call.enqueue(new Callback<BannerImageModleList>() {
            @Override
            public void onResponse(@NonNull Call<BannerImageModleList> call, Response<BannerImageModleList> response) {
                if (response.isSuccessful()){
                    bannerImageLiveData.setValue(response.body());
                    for (BannerImageModel ban: response.body().getData()) {
                        Log.d("bannerImage",ban.getImage()+ "   "+map.get("catId"));
                    }
                }else {
                    Log.d("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<BannerImageModleList> call, Throwable t) {
                Log.d("onResponse error", t.getMessage());
            }
        });
        return bannerImageLiveData;
    }

}
