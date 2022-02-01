package com.sumitapps.sattamatkaguru.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.sumitapps.sattamatkaguru.R;
import com.sumitapps.sattamatkaguru.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {
    ActivityResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.todayResult.setOnClickListener(v -> {
            startActivity(new Intent(ResultActivity.this,TodayResultActivity.class));
        });
        binding.backIcon.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.allResult.setOnClickListener(v -> {
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra("title","All Results");
            intent.putExtra("url","www.google.com");
            startActivity(intent);
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
        finish();
    }
}