package com.sumitapps.sattamatkaguru.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sumitapps.sattamatkaguru.R;
import com.sumitapps.sattamatkaguru.models.CatItemModel;
import com.sumitapps.sattamatkaguru.models.ChartModel;

import java.util.ArrayList;
import java.util.List;

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.ViewHolder> {
    private final List<ChartModel> chartModelList = new ArrayList<>();
    Context context;
    ChartItemInterface chartItemInterface;

    public ChartAdapter(Context context, ChartItemInterface chartItemInterface) {
        this.context = context;
        this.chartItemInterface = chartItemInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.chartNameTxt.setText(chartModelList.get(position).getChartName());
        holder.itemView.setOnClickListener(v -> chartItemInterface.onItemClicked(chartModelList.get(position)));
    }

    @Override
    public int getItemCount() {
        return chartModelList.size();
    }

    public void updateChartItemList(List<ChartModel> chartModels){
        chartModelList.clear();
        chartModelList.addAll(chartModels);

    }

    public interface ChartItemInterface {
        void onItemClicked(ChartModel chartModel);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView chartNameTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chartNameTxt = itemView.findViewById(R.id.chart_name_txt);
        }
    }
}
