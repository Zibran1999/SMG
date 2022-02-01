package com.sumitapps.sattamatkaguru.models;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("fetch_news.php")
    Call<NewsModelList> getAllNews();

    @FormUrlEncoded
    @POST("fetch_today_result.php")
    Call<TodayResultModelList> getAllTodayResult(@FieldMap Map<String,String> map);

    @POST("fetch_charts.php")
    Call<ChartItemModelList> getAllCharts();

    @POST("fetch_category_item.php")
    Call<CatItemModelList> getAllCatItems();

    @FormUrlEncoded
    @POST("fetch_cat_banner_img.php")
    Call<BannerImageModleList> getAllBannerImages(@FieldMap Map<String, String> map);
}
