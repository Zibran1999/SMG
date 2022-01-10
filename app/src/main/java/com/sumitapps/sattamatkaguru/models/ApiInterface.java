package com.sumitapps.sattamatkaguru.models;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("fetch_news.php")
    Call<NewsModelList> getAllNews();

    @POST("fetch_charts.php")
    Call<ChartItemModelList> getAllCharts();

    @POST("fetch_category_item.php")
    Call<CatItemModelList> getAllCatItems();
}
