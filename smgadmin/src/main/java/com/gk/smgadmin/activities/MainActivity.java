package com.gk.smgadmin.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.gk.smgadmin.R;
import com.gk.smgadmin.databinding.ActivityMainBinding;
import com.gk.smgadmin.models.ApiInterface;
import com.gk.smgadmin.models.ApiWebServices;
import com.gk.smgadmin.models.MessageModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Dialog uploadCatDialog, newsUploadDialog, uploadBannerImagesDialog, loadingDialog;
    EditText catNameTxt, catUrlTxt;
    Button uploadCatBtn, cancelBtn, uploadNewsBtn;
    ApiInterface apiInterface;
    Map<String, String> map = new HashMap<>();
    ImageView chooseImage, chooseBannerImage;
    EditText newsTitle, newsDesc, imageTitleTxt, imageURlTxt;
    Bitmap bitmap;
    String encodedImage;
    ActivityResultLauncher<String> launcher;

    public static String imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
        byte[] imageBytes = stream.toByteArray();
        return android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //****Loading Dialog****/
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.item_bg));
        loadingDialog.setCancelable(false);
        //**Loading Dialog****/

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                if (chooseImage != null) {
                    Glide.with(this).load(result).into(chooseImage);
                } else {
                    Glide.with(this).load(result).into(chooseBannerImage);
                }
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(result);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    encodedImage = imageStore(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        apiInterface = ApiWebServices.getApiInterface();
        binding.uploadCatBtn.setOnClickListener(v -> {
            catUploadDialog("Category");
        });
        binding.uploadNewsBtn.setOnClickListener(v -> {
            newsUploadDialog();
        });

        binding.uploadChartBtn.setOnClickListener(v -> {
            catUploadDialog("Chart");
        });

        binding.editAndDelete.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, EditAndDeleteActivity.class));
        });
        binding.editDeleteTodayResult.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, UpdateTodayResultActivity.class));
        });
    }





    private void newsUploadDialog() {
        newsUploadDialog = new Dialog(this);
        newsUploadDialog.setContentView(R.layout.upload_news_dialog);
        newsUploadDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        newsUploadDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.item_bg));
        newsUploadDialog.setCancelable(false);
        newsUploadDialog.show();

        chooseImage = newsUploadDialog.findViewById(R.id.choose_image);
        newsTitle = newsUploadDialog.findViewById(R.id.news_title);
        newsDesc = newsUploadDialog.findViewById(R.id.news_desc);
        uploadNewsBtn = newsUploadDialog.findViewById(R.id.upload_new_data_btn);
        cancelBtn = newsUploadDialog.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(v -> {
            newsUploadDialog.dismiss();
        });
        chooseImage.setOnClickListener(v -> launcher.launch("image/*"));
        uploadNewsBtn.setOnClickListener(v -> {
            loadingDialog.show();
            String nTitle = newsTitle.getText().toString().trim();
            String nDesc = newsDesc.getText().toString().trim();
            if (encodedImage == null) {
                Toast.makeText(MainActivity.this, "Please Select an Image", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(nTitle)) {
                newsTitle.setError("Title Required");
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(nDesc)) {
                newsDesc.setError("Description Required");
                loadingDialog.dismiss();
            } else {
                map.put("img", encodedImage);
                map.put("title", nTitle);
                map.put("desc", nDesc);
                uploadNewsData(map);
            }
        });
    }

    private void uploadNewsData(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.uploadNews(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "News Uploaded", Toast.LENGTH_SHORT).show();
                    newsUploadDialog.dismiss();
                } else {
                    assert response.body() != null;
                    Toast.makeText(MainActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void catUploadDialog(String type) {
        uploadCatDialog = new Dialog(this);
        uploadCatDialog.setContentView(R.layout.upload_cat_dialog);
        uploadCatDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        uploadCatDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.item_bg));
        uploadCatDialog.setCancelable(false);
        uploadCatDialog.show();
        catNameTxt = uploadCatDialog.findViewById(R.id.category_name_txt);
        catUrlTxt = uploadCatDialog.findViewById(R.id.category_link);
        cancelBtn = uploadCatDialog.findViewById(R.id.cancel_b);
        cancelBtn.setOnClickListener(v -> uploadCatDialog.dismiss());
        uploadCatBtn = uploadCatDialog.findViewById(R.id.upload_cat_data);
        if (type.equals("Category")) {
            catNameTxt.setHint("Category Name");
            catUrlTxt.setHint("Category Url");
            catUrlTxt.setVisibility(View.GONE);
        } else {
            catNameTxt.setHint("Chart Name");
            catUrlTxt.setHint("Chart Url");
        }

        uploadCatBtn.setOnClickListener(v -> {
            loadingDialog.show();
            String catName = catNameTxt.getText().toString().trim();
            String catUrl = catUrlTxt.getText().toString().trim();

            if (type.equals("Category")) {

                if (TextUtils.isEmpty(catName)) {
                    catNameTxt.setError("Field Required");
                    loadingDialog.dismiss();
                } else {
                    map.put("catName", catName);
                    uploadCatData(map);
                }

            } else {
                if (TextUtils.isEmpty(catName)) {
                    catNameTxt.setError("Field Required");
                    loadingDialog.dismiss();
                } else if (TextUtils.isEmpty(catUrl)) {
                    catUrlTxt.setError("Filed Required");
                    loadingDialog.dismiss();
                } else {
                    map.put("chartName", catName);
                    map.put("chartUrl", catUrl);
                    uploadChartData(map);
                }
            }
        });

    }

    private void uploadCatData(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.uploadCategory(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {

                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    uploadCatDialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                Log.d("onResponse", t.getMessage());
            }
        });
    }

    private void uploadChartData(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.uploadChart(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {

                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                    uploadCatDialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                Log.d("onResponse", t.getMessage());
            }
        });
    }
}