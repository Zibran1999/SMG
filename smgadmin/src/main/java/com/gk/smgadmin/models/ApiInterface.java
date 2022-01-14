package com.gk.smgadmin.models;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiInterface {


    @FormUrlEncoded
    @POST("upload_category.php")
    Call<MessageModel> uploadCategory(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("upload_banner_images.php")
    Call<MessageModel> uploadBannerImage(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("upload_today_result.php")
    Call<MessageModel> uploadTodayResult(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("upload_chart.php")
    Call<MessageModel> uploadChart(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("upload_news.php")
    Call<MessageModel> uploadNews(@FieldMap Map<String, String> map);

    // Fetching Data

    @POST("fetch_news.php")
    Call<NewsModelList> getAllNews();

    @POST("fetch_charts.php")
    Call<ChartItemModelList> getAllCharts();

    @POST("fetch_category_item.php")
    Call<CatItemModelList> getAllCatItems();

    @POST("fetch_today_result.php")
    Call<TodayResultModelList> getAllTodayResult();

    // Delete Data

    @FormUrlEncoded
    @POST("delete_api.php")
    Call<MessageModel> deleteData(@FieldMap Map<String, String> map);

    @POST("fetch_banner_images.php")
    Call<BannerImageModleList> getAllBannerImages();

    // Update Data

    @FormUrlEncoded
    @POST("update_banner_image.php")
    Call<MessageModel> updateBannerImg(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("update_news.php")
    Call<MessageModel> updateNews(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("update_today_result.php")
    Call<MessageModel> updateTodayResult(@FieldMap Map<String, String> map);
}
