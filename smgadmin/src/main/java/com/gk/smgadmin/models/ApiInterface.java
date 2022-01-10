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
    @POST("upload_today_result.php")
    Call<MessageModel> uploadTodayResult(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("upload_chart.php")
    Call<MessageModel> uploadChart(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("upload_news.php")
    Call<MessageModel> uploadNews(@FieldMap Map<String, String> map);

}
