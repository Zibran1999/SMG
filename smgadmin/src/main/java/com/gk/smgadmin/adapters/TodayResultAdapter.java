package com.gk.smgadmin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.gk.smgadmin.R;
import com.gk.smgadmin.models.TodayResultModel;

import java.util.ArrayList;
import java.util.List;

public class TodayResultAdapter extends RecyclerView.Adapter<TodayResultAdapter.ViewHolder> {
    List<TodayResultModel> todayResultModelList = new ArrayList<>();
    TodayResultInterface todayResultInterface;

    public TodayResultAdapter(TodayResultInterface todayResultInterface) {
        this.todayResultInterface = todayResultInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_result_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.todayResultTitle.setText(todayResultModelList.get(position).getResName());
        holder.todayResultTime.setText("Result Time "+todayResultModelList.get(position).getResultTime());
        holder.todayResultNo.setText("New Result\n"+"{"+todayResultModelList.get(position).getNewNo()+"}");
        holder.itemView.setOnClickListener(v -> todayResultInterface.onItemClicked(todayResultModelList.get(position)));
    }

    @Override
    public int getItemCount() {
        return todayResultModelList.size();
    }
    public void updateTodayResultList(List<TodayResultModel> todayResultModels){
        todayResultModelList.clear();
        todayResultModelList.addAll(todayResultModels);

    }

    public interface TodayResultInterface{
        void onItemClicked(TodayResultModel todayResultModel);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView todayResultTitle,todayResultTime,todayResultNo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todayResultTitle = itemView.findViewById(R.id.today_result_title);
            todayResultTime = itemView.findViewById(R.id.today_result_time);
            todayResultNo = itemView.findViewById(R.id.today_result_no);
        }
    }
}
