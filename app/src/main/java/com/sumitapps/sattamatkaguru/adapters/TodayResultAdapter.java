package com.sumitapps.sattamatkaguru.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.sumitapps.sattamatkaguru.R;
import com.sumitapps.sattamatkaguru.models.TodayResultModel;
import com.sumitapps.sattamatkaguru.utils.MyDillUtilCallBack;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_result_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.todayResultTitle.setText(todayResultModelList.get(position).getResName());
        holder.todayResultTime.setText("Result Time " + todayResultModelList.get(position).getResultTime());
        holder.todayResultNo.setText("New Result\n" + "{" + todayResultModelList.get(position).getNewNo() + "}");
        holder.lottieAnimationView.setOnClickListener(v -> todayResultInterface.onItemClicked(todayResultModelList.get(position)));
    }

    @Override
    public int getItemCount() {
        return todayResultModelList.size();
    }

    public void updateTodayResultList(List<TodayResultModel> todayResultModels) {

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDillUtilCallBack(todayResultModelList, todayResultModels));
        diffResult.dispatchUpdatesTo(this);
        todayResultModelList.clear();

        todayResultModelList.addAll(todayResultModels);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);

        } else {
            Bundle bundle = (Bundle) payloads.get(0);
            for (String key : bundle.keySet()) {
                if (key.equals("NewNo")) {
                    holder.todayResultNo.setText("New Result\n" + "{" + bundle.getString("NewNo") + "}");

                }
            }
        }
    }

    public interface TodayResultInterface {
        void onItemClicked(TodayResultModel todayResultModel);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView todayResultTitle, todayResultTime, todayResultNo;
        LottieAnimationView lottieAnimationView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todayResultTitle = itemView.findViewById(R.id.today_result_title);
            todayResultTime = itemView.findViewById(R.id.today_result_time);
            todayResultNo = itemView.findViewById(R.id.today_result_no);
            lottieAnimationView = itemView.findViewById(R.id.lottie_whatsapp);
        }
    }
}
