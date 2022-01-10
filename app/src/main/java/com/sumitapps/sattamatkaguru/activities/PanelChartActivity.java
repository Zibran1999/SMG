package com.sumitapps.sattamatkaguru.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sumitapps.sattamatkaguru.R;
import com.sumitapps.sattamatkaguru.adapters.ChartAdapter;
import com.sumitapps.sattamatkaguru.databinding.ActivityPanelChartBinding;
import com.sumitapps.sattamatkaguru.models.ChartItemModelList;
import com.sumitapps.sattamatkaguru.models.ChartModel;
import com.sumitapps.sattamatkaguru.models.PageViewModel;
import com.sumitapps.sattamatkaguru.utils.CommonMethod;

import java.util.ArrayList;
import java.util.List;

public class PanelChartActivity extends AppCompatActivity implements ChartAdapter.ChartItemInterface {
    ActivityPanelChartBinding binding;
    Dialog loadingDialog;
    PageViewModel pageViewModel;
    List<ChartModel> chartModels = new ArrayList<>();
    ChartAdapter chartAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPanelChartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadingDialog = CommonMethod.getLoadingDialog(this);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.penalRecyclerView.setLayoutManager(layoutManager);
        binding.backIcon.setOnClickListener(v -> {
            onBackPressed();
        });

        setPenalChart();
    }

    private void setPenalChart() {
        loadingDialog.show();
        pageViewModel.getChartItemList().observe(this, chartItemModelList -> {
            if (chartItemModelList!=null){
                chartModels.clear();
                chartModels.addAll(chartItemModelList.getData());
                Log.d("ChartData",chartItemModelList.getData().toString());
                Log.d("chartTitle",chartItemModelList.getData().get(0).getChartName().toString());
                chartAdapter = new ChartAdapter(this,this);
                binding.penalRecyclerView.setAdapter(chartAdapter);
                chartAdapter.updateChartItemList(chartModels);
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClicked(ChartModel chartModel) {
        Intent intent = new Intent(this,WebViewActivity.class);
        intent.putExtra("title",chartModel.getChartName());
        intent.putExtra("url",chartModel.getChartUrl());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
        finish();
    }
}