package com.gk.smgadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gk.smgadmin.R;
import com.gk.smgadmin.models.BannerImageModel;
import com.gk.smgadmin.models.CatItemModel;

import java.util.ArrayList;
import java.util.List;

public class BannerImageAdapter extends RecyclerView.Adapter<BannerImageAdapter.ViewHolder> {

    List<BannerImageModel> bannerImageModelList = new ArrayList<>();
    Context context;
    BannerImageInterface bannerImageInterface;

    public BannerImageAdapter(Context context, BannerImageInterface bannerImageInterface) {
        this.context = context;
        this.bannerImageInterface = bannerImageInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_image_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load("https://gedgetsworld.in/SM_Guru/Banner_Images/"+bannerImageModelList.get(position).getImage()).into(holder.bannerImageView);
        holder.bannerTitle.setText(bannerImageModelList.get(position).getTitle());
        holder.itemView.setOnClickListener(v -> {
            bannerImageInterface.onItemClicked(bannerImageModelList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return bannerImageModelList.size();
    }

    public void updateBannerImageList(List<BannerImageModel> bannerImageModels){
        bannerImageModelList.clear();
        bannerImageModelList.addAll(bannerImageModels);
    }

    public interface BannerImageInterface{
        void onItemClicked(BannerImageModel bannerImageModel);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImageView;
        TextView bannerTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImageView = itemView.findViewById(R.id.banner_imageview);
            bannerTitle = itemView.findViewById(R.id.banner_img_title);
        }
    }
}
