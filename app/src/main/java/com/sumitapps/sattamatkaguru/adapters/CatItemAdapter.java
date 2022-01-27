package com.sumitapps.sattamatkaguru.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.sumitapps.sattamatkaguru.R;
import com.sumitapps.sattamatkaguru.models.CatItemModel;

import java.util.ArrayList;
import java.util.List;

public class CatItemAdapter extends RecyclerView.Adapter<CatItemAdapter.ViewHolder> {

    private final List<CatItemModel> catItemModelList = new ArrayList<>();
    Activity homeActivity;
    CatItemInterface catItemInterface;
    Application application;
    int index = 0;
    String name;


    public CatItemAdapter(String name,Activity context, CatItemInterface catItemInterface, Application application) {
        this.catItemInterface = catItemInterface;
        this.application = application;
        homeActivity = context;
        this.name = name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        holder.catItemName.setText(catItemModelList.get(position).getCatName());
        holder.itemView.setOnClickListener(v -> {
            index = holder.getAdapterPosition();
            catItemInterface.onItemClicked(catItemModelList.get(position));
            notifyDataSetChanged();
        });

        Log.d("posit"," "+index+ " "+position);
        if (!name.equals("Home")){
            if (index == position) {
                holder.itemLayout.setCardBackgroundColor(ContextCompat.getColor(homeActivity,R.color.purple_700));
                holder.catItemName.setTextColor(ContextCompat.getColor(homeActivity,R.color.white));
            }else {
                holder.itemLayout.setCardBackgroundColor(ContextCompat.getColor(homeActivity,R.color.white));
                holder.catItemName.setTextColor(ContextCompat.getColor(homeActivity,R.color.black));
            }
        }


    }

    @Override
    public int getItemCount() {
        return catItemModelList.size();
    }

    public void updateCatItemList(List<CatItemModel> catItemModels) {
        catItemModelList.clear();
        catItemModelList.addAll(catItemModels);
    }

    public interface CatItemInterface {
        void onItemClicked(CatItemModel catItemModel);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView catItemName;
        MaterialCardView itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catItemName = itemView.findViewById(R.id.cat_item_name_txt);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }
}
