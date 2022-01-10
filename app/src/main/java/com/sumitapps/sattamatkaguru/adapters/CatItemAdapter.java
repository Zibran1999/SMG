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

import java.util.ArrayList;
import java.util.List;

public class CatItemAdapter extends RecyclerView.Adapter<CatItemAdapter.ViewHolder> {

    private final List<CatItemModel> catItemModelList = new ArrayList<>();
    Context context;
    CatItemInterface catItemInterface;

    public CatItemAdapter(Context context, CatItemInterface catItemInterface) {
        this.context = context;
        this.catItemInterface = catItemInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.catItemName.setText(catItemModelList.get(position).getCatName());
        holder.itemView.setOnClickListener(v -> catItemInterface.onItemClicked(catItemModelList.get(position)));
    }

    @Override
    public int getItemCount() {
        return catItemModelList.size();
    }

    public void updateCatItemList(List<CatItemModel> catItemModels){
        catItemModelList.clear();
        catItemModelList.addAll(catItemModels);
    }

    public interface CatItemInterface {
        void onItemClicked(CatItemModel catItemModel);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView catItemName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catItemName = itemView.findViewById(R.id.cat_item_name_txt);
        }
    }
}
