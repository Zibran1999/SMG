package com.sumitapps.sattamatkaguru.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sumitapps.sattamatkaguru.R;
import com.sumitapps.sattamatkaguru.adapters.NewsAdapter;
import com.sumitapps.sattamatkaguru.databinding.ActivityNewsBinding;
import com.sumitapps.sattamatkaguru.models.NewsModel;
import com.sumitapps.sattamatkaguru.models.PageViewModel;
import com.sumitapps.sattamatkaguru.utils.CommonMethod;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements NewsAdapter.NewsInterface {
    ActivityNewsBinding binding;
    RecyclerView newsRecyclerView;
    List<NewsModel> newsModelList = new ArrayList<>();
    NewsAdapter newsAdapter;
    private PageViewModel pageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backIcon.setOnClickListener(v -> {
            onBackPressed();
        });
        newsRecyclerView = findViewById(R.id.news_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        newsRecyclerView.setLayoutManager(layoutManager);
        pageViewModel = new ViewModelProvider(NewsActivity.this).get(PageViewModel.class);

        setNewsData(this);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            setNewsData(this);
            binding.swipeRefresh.setRefreshing(false);
        });
    }

    private void setNewsData(Context context) {
        newsAdapter = new NewsAdapter(context, this);
        newsRecyclerView.setAdapter(newsAdapter);

        pageViewModel.getNews().observe(NewsActivity.this, newsModelList -> {

            if (!newsModelList.getData().isEmpty()) {
                newsAdapter.updateNewsList(newsModelList.getData());
            }
            CommonMethod.getLoadingDialog(this).dismiss();
        });

    }

    @Override
    public void onItemClicked(NewsModel newsModel) {
        Intent intent = new Intent(NewsActivity.this, NewsDataActivity.class);
        intent.putExtra("img", newsModel.getImage());
        intent.putExtra("title", newsModel.getTitle());
        intent.putExtra("desc", newsModel.getDesc());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
        finish();
    }
}