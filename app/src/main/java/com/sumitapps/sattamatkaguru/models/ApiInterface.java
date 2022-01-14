package com.sumitapps.sattamatkaguru.models;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("fetch_news.php")
    Call<NewsModelList> getAllNews();

    @POST("fetch_today_result.php")
    Call<TodayResultModelList> getAllTodayResult();

    @POST("fetch_charts.php")
    Call<ChartItemModelList> getAllCharts();

    @POST("fetch_category_item.php")
    Call<CatItemModelList> getAllCatItems();

    @POST("fetch_banner_images.php")
    Call<BannerImageModleList> getAllBannerImages();
}
