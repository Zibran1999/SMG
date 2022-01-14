package com.gk.smgadmin.activities;

import static com.gk.smgadmin.activities.MainActivity.imageStore;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gk.smgadmin.R;
import com.gk.smgadmin.adapters.BannerImageAdapter;
import com.gk.smgadmin.adapters.CatItemAdapter;
import com.gk.smgadmin.adapters.ChartAdapter;
import com.gk.smgadmin.adapters.NewsAdapter;
import com.gk.smgadmin.databinding.ActivityEditAndDeleteBinding;
import com.gk.smgadmin.models.ApiInterface;
import com.gk.smgadmin.models.ApiWebServices;
import com.gk.smgadmin.models.BannerImageModel;
import com.gk.smgadmin.models.CatItemModel;
import com.gk.smgadmin.models.ChartModel;
import com.gk.smgadmin.models.MessageModel;
import com.gk.smgadmin.models.NewsModel;
import com.gk.smgadmin.models.PageViewModel;
import com.gk.smgadmin.utils.CommonMethod;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAndDeleteActivity extends AppCompatActivity implements CatItemAdapter.CatItemInterface, ChartAdapter.ChartItemInterface, BannerImageAdapter.BannerImageInterface, NewsAdapter.NewsInterface {
    ActivityEditAndDeleteBinding binding;
    Dialog loadingDialog;
    PageViewModel pageViewModel;
    List<CatItemModel> catItemModels = new ArrayList<>();
    List<ChartModel> chartModels = new ArrayList<>();
    List<BannerImageModel> bannerImageModelList = new ArrayList<>();
    List<NewsModel> newsModels = new ArrayList<>();
    CatItemAdapter catItemAdapter;
    ChartAdapter chartAdapter;
    NewsAdapter newsAdapter;
    BannerImageAdapter bannerImageAdapter;
    ApiInterface apiInterface;
    Map<String, String> map = new HashMap<>();
    String itemId, encodedImage, bannerImageUrl;
    Dialog uploadBannerImagesDialog,newsUploadDialog;
    ImageView chooseBannerImage,chooseImage;
    EditText imageTitleTxt, imageURlTxt,newsTitle,newsDesc;
    Button uploadBannerImageBtn, cancelBtn,uploadNewsBtn;
    ActivityResultLauncher<String> launcher;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditAndDeleteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadingDialog = CommonMethod.getLoadingDialog(this);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        apiInterface = ApiWebServices.getApiInterface();
        fetchCategory();
        fetchPenalChartItems();
        fetchBannerImages();
        fetchNews();

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                if (chooseBannerImage!=null) {
                    Glide.with(this).load(result).into(chooseBannerImage);
                }else{
                    Glide.with(this).load(result).into(chooseImage);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.categoryRecyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        binding.penalChartRecyclerview.setLayoutManager(layoutManager2);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        layoutManager3.setOrientation(RecyclerView.HORIZONTAL);
        binding.bannerImagesRecyclerview.setLayoutManager(layoutManager3);

        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this);
        layoutManager4.setOrientation(RecyclerView.HORIZONTAL);
        binding.newsRecyclerview.setLayoutManager(layoutManager4);
    }

    private void fetchNews() {
        loadingDialog.show();
        pageViewModel.getNews().observe(this, newsModelList -> {
            if (newsModelList != null){
                newsModels.clear();
                newsModels.addAll(newsModelList.getData());
                newsAdapter = new NewsAdapter(this,this);
                binding.newsRecyclerview.setAdapter(newsAdapter);
                newsAdapter.updateNewsList(newsModels);
                loadingDialog.dismiss();
            }
        });
    }

    private void fetchBannerImages() {
        loadingDialog.show();
        pageViewModel.getBannerImageList().observe(this, bannerImageModleList -> {
            if (bannerImageModleList != null) {
                bannerImageModelList.clear();
                bannerImageModelList.addAll(bannerImageModleList.getData());
                bannerImageAdapter = new BannerImageAdapter(this, this);
                binding.bannerImagesRecyclerview.setAdapter(bannerImageAdapter);
                bannerImageAdapter.updateBannerImageList(bannerImageModelList);
                loadingDialog.dismiss();
            }
        });
    }

    private void fetchPenalChartItems() {
        loadingDialog.show();
        pageViewModel.getChartItemList().observe(this, chartItemModelList -> {
            if (chartItemModelList != null) {
                chartModels.clear();
                chartModels.addAll(chartItemModelList.getData());
                chartAdapter = new ChartAdapter(this, this);
                binding.penalChartRecyclerview.setAdapter(chartAdapter);
                chartAdapter.updateChartItemList(chartModels);
                loadingDialog.dismiss();
            }
        });
    }

    private void fetchCategory() {
        loadingDialog.show();
        pageViewModel.getCatItemList().observe(this, catItemModelList -> {
            if (catItemModelList != null) {
                catItemModels.clear();
                catItemModels.addAll(catItemModelList.getData());
                catItemAdapter = new CatItemAdapter(this, this);
                binding.categoryRecyclerView.setAdapter(catItemAdapter);
                catItemAdapter.updateCatItemList(catItemModels);
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClicked(CatItemModel catItemModel) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Delete Category");
        builder.setMessage("Do you want to delete this item?");
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        }).setPositiveButton("Delete", (dialog, which) -> {
            loadingDialog.show();
            itemId = catItemModel.getId();
            map.put("id", itemId);
            map.put("title", "category");
            deleteData(map, "category");
        });
        builder.show();
    }

    @Override
    public void onItemClicked(ChartModel chartModel) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Delete Penal Chart");
        builder.setMessage("Do you want to delete this item?");
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        }).setPositiveButton("Delete", (dialog, which) -> {
            loadingDialog.show();
            itemId = chartModel.getId();
            map.put("id", itemId);
            map.put("title", "chart");
            deleteData(map, "chart");
        });
        builder.show();
    }

    @Override
    public void onItemClicked(BannerImageModel bannerImageModel) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Edit & Delete Banner");
        builder.setMessage("Edit or Delete?");
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        }).setPositiveButton("Delete", (dialog, which) -> {
            loadingDialog.show();
            itemId = bannerImageModel.getId();
            map.put("id", itemId);
            map.put("path","Banner_Images/"+bannerImageModel.getImage());
            map.put("title", "banner");
            deleteData(map, "banner");

        }).setNeutralButton("Edit", (dialog, which) -> {
            itemId = bannerImageModel.getId();
            String img = bannerImageModel.getImage();
            String imgTitle = bannerImageModel.getTitle();
            String imgUrl = bannerImageModel.getImageUrl();
            uploadBannerImagesDialog(itemId, img, imgTitle, imgUrl);
        }).show();
    }

    @Override
    public void onItemClicked(NewsModel newsModel) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Edit & Delete News");
        builder.setMessage("Edit or Delete?");
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        }).setPositiveButton("Delete", (dialog, which) -> {
            loadingDialog.show();
            itemId = newsModel.getId();
            map.put("id",itemId);
            map.put("path","News_Images/"+newsModel.getImage());
            map.put("title","news");
            deleteData(map,"news");

        }).setNeutralButton("Edit", (dialog, which) -> {
            itemId = newsModel.getId();
            String img = newsModel.getImage();
            String title = newsModel.getTitle();
            String desc = newsModel.getDesc();
            newsUploadDialog(itemId,img,title,desc);
        }).show();
    }

    public void uploadBannerImagesDialog(String itemId, String img, String imgTitle, String imgUrl) {
        uploadBannerImagesDialog = new Dialog(this);
        uploadBannerImagesDialog.setContentView(R.layout.upload_image_dialog);
        uploadBannerImagesDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        uploadBannerImagesDialog.getWindow().setBackgroundDrawable(
                ContextCompat.getDrawable(this, R.drawable.item_bg));
        uploadBannerImagesDialog.setCancelable(false);
        uploadBannerImagesDialog.show();

        chooseBannerImage = uploadBannerImagesDialog.findViewById(R.id.choose_banner_image);
        imageTitleTxt = uploadBannerImagesDialog.findViewById(R.id.image_title);
        imageURlTxt = uploadBannerImagesDialog.findViewById(R.id.image_url);
        uploadBannerImageBtn = uploadBannerImagesDialog.findViewById(R.id.upload_image_btn);
        cancelBtn = uploadBannerImagesDialog.findViewById(R.id.image_cancel);
        cancelBtn.setOnClickListener(v -> {
            uploadBannerImagesDialog.dismiss();
            encodedImage = "";
        });

        encodedImage = img;
        Glide.with(this).load("https://gedgetsworld.in/SM_Guru/Banner_Images/" + img).into(chooseBannerImage);
        imageTitleTxt.setText(imgTitle);
        imageURlTxt.setText(imgUrl);
        chooseBannerImage.setOnClickListener(v -> {
            launcher.launch("image/*");
        });

        uploadBannerImageBtn.setOnClickListener(v -> {
            loadingDialog.show();
            String mTitle = imageTitleTxt.getText().toString().trim();
            String mUrl = imageURlTxt.getText().toString().trim();
            if (encodedImage == null) {
                Toast.makeText(EditAndDeleteActivity.this, "Please Select an Image", Toast.LENGTH_SHORT).show();

            } else if (TextUtils.isEmpty(mTitle)) {
                imageTitleTxt.setError("Title Required");
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(mUrl)) {
                imageURlTxt.setError("Image Url Required");
                loadingDialog.dismiss();
            } else {
                if (encodedImage.length() <= 100) {
                    map.put("id", itemId);
                    map.put("img", encodedImage);
                    map.put("title", mTitle);
                    map.put("deleteImg", img);
                    map.put("url", mUrl);
                    map.put("imgKey", "0");
                    updateBannerImage(map);

                } else {
                    map.put("id", itemId);
                    map.put("img", encodedImage);
                    map.put("title", mTitle);
                    map.put("deleteImg", img);
                    map.put("url", mUrl);
                    map.put("imgKey", "1");
                    updateBannerImage(map);
                }

            }
        });

    }

    private void newsUploadDialog(String itemId, String img, String title, String desc) {
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
        encodedImage = img;
        Glide.with(this).load("https://gedgetsworld.in/SM_Guru/News_Images/"+img).into(chooseImage);
        newsTitle.setText(title);
        newsDesc.setText(desc);
        chooseImage.setOnClickListener(v -> launcher.launch("image/*"));
        uploadNewsBtn.setOnClickListener(v -> {
            loadingDialog.show();
            String nTitle = newsTitle.getText().toString().trim();
            String nDesc = newsDesc.getText().toString().trim();
            if (encodedImage == null) {
                Toast.makeText(EditAndDeleteActivity.this, "Please Select an Image", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(nTitle)) {
                newsTitle.setError("Title Required");
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(nDesc)) {
                newsDesc.setError("Description Required");
                loadingDialog.dismiss();
            } else {
                if (encodedImage.length() <= 100) {
                    map.put("id",itemId);
                    map.put("img", encodedImage);
                    map.put("title", nTitle);
                    map.put("desc", nDesc);
                    map.put("deleteImg",img);
                    map.put("imgKey","0");
                    updateNewsData(map);
                }else {
                    map.put("id",itemId);
                    map.put("img", encodedImage);
                    map.put("title", nTitle);
                    map.put("desc", nDesc);
                    map.put("deleteImg",img);
                    map.put("imgKey","1");
                    updateNewsData(map);
                }
            }
        });
    }

    private void updateNewsData(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.updateNews(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(EditAndDeleteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    fetchNews();
                    newsUploadDialog.dismiss();
                } else {
                    Toast.makeText(EditAndDeleteActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(EditAndDeleteActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void updateBannerImage(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.updateBannerImg(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(EditAndDeleteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    fetchBannerImages();
                    uploadBannerImagesDialog.dismiss();
                } else {
                    Toast.makeText(EditAndDeleteActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(EditAndDeleteActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void deleteData(Map<String, String> map, String type) {
        Call<MessageModel> call = apiInterface.deleteData(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    switch (type) {
                        case "category":
                            fetchCategory();
                            break;
                        case "chart":
                            fetchPenalChartItems();
                            break;
                        case "banner":
                            fetchBannerImages();
                            break;
                        case "news":
                            fetchNews();
                            break;
                    }

                } else {
                    Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }
}