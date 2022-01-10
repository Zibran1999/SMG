package com.sumitapps.sattamatkaguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sumitapps.sattamatkaguru.R;
import com.sumitapps.sattamatkaguru.databinding.ActivityNewsDataBinding;

public class NewsDataActivity extends AppCompatActivity {
    ActivityNewsDataBinding binding;
    String imgUrl,newsTitle,newsDesc;
    ImageView newsImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        newsImage = findViewById(R.id.newsDataImg);

        imgUrl = getIntent().getStringExtra("img");
        newsTitle = getIntent().getStringExtra("title");
        newsDesc = getIntent().getStringExtra("desc");

        binding.title.setText(newsTitle);
        binding.backIcon.setOnClickListener(v -> {
            onBackPressed();
        });
        Glide.with(this).load("https://gedgetsworld.in/SM_Guru/News_Images/" + imgUrl).into(newsImage);
        newsImage.setVisibility(View.VISIBLE);
        binding.newsTitle.setText(newsTitle);
        binding.newsDesc.setText(newsDesc);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
        finish();
    }
}