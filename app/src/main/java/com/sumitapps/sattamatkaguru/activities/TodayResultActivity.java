package com.sumitapps.sattamatkaguru.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.sumitapps.sattamatkaguru.BuildConfig;
import com.sumitapps.sattamatkaguru.R;
import com.sumitapps.sattamatkaguru.adapters.CatItemAdapter;
import com.sumitapps.sattamatkaguru.adapters.TodayResultAdapter;
import com.sumitapps.sattamatkaguru.databinding.ActivityTodayResultBinding;
import com.sumitapps.sattamatkaguru.models.BannerImageModel;
import com.sumitapps.sattamatkaguru.models.CatItemModel;
import com.sumitapps.sattamatkaguru.models.ModelFactory;
import com.sumitapps.sattamatkaguru.models.PageViewModel;
import com.sumitapps.sattamatkaguru.models.TodayPageViewModel;
import com.sumitapps.sattamatkaguru.models.TodayResultModel;
import com.sumitapps.sattamatkaguru.utils.CommonMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodayResultActivity extends AppCompatActivity implements TodayResultAdapter.TodayResultInterface, CatItemAdapter.CatItemInterface {
    ActivityTodayResultBinding binding;
    Dialog loadingDialog;
    PageViewModel pageViewModel;
    List<TodayResultModel> todayResultModels = new ArrayList<>();
    List<BannerImageModel> bannerImageModelList2 = new ArrayList<>();
    TodayResultAdapter todayResultAdapter;
    ImageSlider imageSlider;
    String url1;
    List<SlideModel> slideModels = new ArrayList<>();

    CatItemAdapter catItemAdapter;
    List<CatItemModel> catItemModels = new ArrayList<>();
    TodayPageViewModel pageViewModel2;

    Map<String, String> map = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTodayResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadingDialog = CommonMethod.getLoadingDialog(this);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.showTodayResultRecyclerview.setLayoutManager(layoutManager);
        binding.backIcon.setOnClickListener(v -> {
            onBackPressed();
        });
        imageSlider = findViewById(R.id.image_slider);

        fetchCatItems();
//        showBannerImages();
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchCatItems();
//            showBannerImages();
            binding.swipeRefreshLayout.setRefreshing(false);
        });

    }


    private void showTodayResult(String id) {
        map.put("catId", id);
        bannerImageModelList2.clear();
        pageViewModel2 = new ViewModelProvider(this, new ModelFactory(getApplication(), map)).get(TodayPageViewModel.class);

        loadingDialog.show();
        pageViewModel2.getTodayResultList().observe(TodayResultActivity.this, todayResultModelList -> {
            if (todayResultModelList != null) {
                todayResultModels.clear();
                todayResultModels.addAll(todayResultModelList.getData());
                Log.d("TodayResultData", todayResultModelList.getData().toString());
                todayResultAdapter = new TodayResultAdapter(this);
                todayResultAdapter.updateTodayResultList(todayResultModels);
                binding.showTodayResultRecyclerview.setAdapter(todayResultAdapter);
                todayResultAdapter.updateTodayResultList(todayResultModels);
                loadingDialog.dismiss();
            }

        });
    }
    private void fetchCatItems() {
        loadingDialog.show();
        pageViewModel.getCatItemList().observe(this, catItemModelList -> {
            if (catItemModelList != null) {
                catItemModels.clear();
                catItemModels.addAll(catItemModelList.getData());
                Log.d("CatData", catItemModelList.getData().toString());
                catItemAdapter = new CatItemAdapter("Today",this, this, getApplication());
                binding.showTodayResultRecyclerview.setAdapter(catItemAdapter);
                catItemAdapter.updateCatItemList(catItemModels);
                loadingDialog.dismiss();
                slideModels.clear();
                showTodayResult(catItemModelList.getData().get(0).getId());
                BannerImage(catItemModelList.getData().get(0).getId(), catItemModelList.getData().get(0).getCatName());
                binding.swipeRefreshLayout.setOnRefreshListener(() -> {
                    BannerImage(catItemModelList.getData().get(0).getId(), catItemModelList.getData().get(0).getCatName());
                    showTodayResult(catItemModelList.getData().get(0).getId());
                    binding.swipeRefreshLayout.setRefreshing(false);
                });


            }
        });
    }

    @Override
    public void onItemClicked(TodayResultModel todayResultModel) {
//        देशावर(आज का रिजल्ट =54) यहाँ से अप्प इनस्टॉल करें
        String shareMessage = todayResultModel.getResName() + " (आज का रिजल्ट = " + todayResultModel.getNewNo() + ") यहाँ से " + getString(R.string.app_name) + " App इनस्टॉल करें\n\n";


        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
//            String shareMessage = "\nLet me recommend you this application\n\n";

            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @SuppressLint("QueryPermissionsNeeded")
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onItemClicked(CatItemModel catItemModel) {
        slideModels.clear();
        BannerImage(catItemModel.getId(), catItemModel.getCatName());
        showTodayResult(catItemModel.getId());

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            BannerImage(catItemModel.getId(), catItemModel.getCatName());
            showTodayResult(catItemModel.getId());
            binding.swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void BannerImage(String id, String catName) {
        map.put("catId", id);
        bannerImageModelList2.clear();
        pageViewModel2 = new ViewModelProvider(this, new ModelFactory(getApplication(), map)).get(TodayPageViewModel.class);
        pageViewModel2.getBannerImageList().observe(this, bannerImageModelList -> {

            if (bannerImageModelList != null) {
                slideModels.clear();
                binding.todayResultTitle.setText(catName);
                for (BannerImageModel bannerImageModel : bannerImageModelList.getData()) {
                    Log.d("banner Image2", bannerImageModel.getImage());
                    bannerImageModelList2.addAll(bannerImageModelList.getData());
                    slideModels.add(new SlideModel("https://gedgetsworld.in/SM_Guru/Banner_Images/" + bannerImageModel.getImage(), ScaleTypes.FIT));
                }
                Log.d("ban", String.valueOf(slideModels.size()));
                imageSlider.setImageList(slideModels);
                imageSlider.setItemClickListener(i -> {
                    url1 = bannerImageModelList2.get(i).getImageUrl();
                    openWebPage(url1);
                });
                loadingDialog.dismiss();

            }
        });

    }


}