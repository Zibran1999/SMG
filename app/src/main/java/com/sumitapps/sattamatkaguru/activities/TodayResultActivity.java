package com.sumitapps.sattamatkaguru.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.sumitapps.sattamatkaguru.R;
import com.sumitapps.sattamatkaguru.adapters.TodayResultAdapter;
import com.sumitapps.sattamatkaguru.databinding.ActivityTodayResultBinding;
import com.sumitapps.sattamatkaguru.models.BannerImageModel;
import com.sumitapps.sattamatkaguru.models.PageViewModel;
import com.sumitapps.sattamatkaguru.models.TodayResultModel;
import com.sumitapps.sattamatkaguru.utils.CommonMethod;

import java.util.ArrayList;
import java.util.List;

public class TodayResultActivity extends AppCompatActivity implements TodayResultAdapter.TodayResultInterface {
    ActivityTodayResultBinding binding;
    Dialog loadingDialog;
    PageViewModel pageViewModel;
    List<TodayResultModel> todayResultModels = new ArrayList<>();
    List<BannerImageModel> bannerImageModelList2 = new ArrayList<>();
    TodayResultAdapter todayResultAdapter;
    ImageSlider imageSlider;
    String url1;
    List<SlideModel> slideModels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTodayResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadingDialog = CommonMethod.getLoadingDialog(this);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.showTodayResultRecyclerview.setLayoutManager(layoutManager);
        binding.backIcon.setOnClickListener(v -> {
            onBackPressed();
        });
        showTodayResult();
        showBannerImages();
        imageSlider = findViewById(R.id.image_slider);
    }

    private void showBannerImages() {
        loadingDialog.show();
        pageViewModel.getBannerImageList().observe(TodayResultActivity.this, bannerImageModleList -> {
            if (bannerImageModleList != null) {
                bannerImageModelList2.clear();
                bannerImageModelList2.addAll(bannerImageModleList.getData());
                slideModels.clear();
                for (BannerImageModel bannerImageModel : bannerImageModleList.getData()) {
                    slideModels.add(new SlideModel("https://gedgetsworld.in/SM_Guru/Banner_Images/" + bannerImageModel.getImage(), bannerImageModel.getTitle(), ScaleTypes.CENTER_INSIDE));
                }

//                slideModels.add(new SlideModel("https://bit.ly/2YoJ77H", "The animal population decreased by 58 percent in 42 years.", ScaleTypes.CENTER_INSIDE));
//                slideModels.add(new SlideModel("https://bit.ly/2BteuF2", "Elephants and tigers may become extinct.", ScaleTypes.CENTER_INSIDE));
//                slideModels.add(new SlideModel("https://bit.ly/3fLJf72", "And people do that.", ScaleTypes.CENTER_INSIDE));

                imageSlider.setImageList(slideModels);
                imageSlider.setItemClickListener(i -> {
                    url1 = bannerImageModelList2.get(i).getImageUrl();
                   openWebPage(url1);
                });
            }
        });


    }

    private void showTodayResult() {
        loadingDialog.show();
        pageViewModel.getTodayResultList().observe(TodayResultActivity.this, todayResultModelList -> {
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

    @Override
    public void onItemClicked(TodayResultModel todayResultModel) {

    }
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
}