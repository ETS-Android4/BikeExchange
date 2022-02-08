package com.hfad.bikeexchange.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.SingleCategoryActivity;
import com.hfad.bikeexchange.models.Bike;

import java.util.ArrayList;

public class CategoriesImagesAdapter extends
        RecyclerView.Adapter<CategoriesImagesAdapter.ViewHolder> {

    private final ArrayList<Bike> bikesList;
    private final Context mContext;

    public CategoriesImagesAdapter(Context mContext, ArrayList<Bike> bikesList) {
        this.mContext = mContext;
        this.bikesList = bikesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.categories_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bike bike = bikesList.get(position);

        //ImageView : Glide Library
        Glide.with(mContext).load(bikesList.get(position).getImage())
                .into(holder.cardView);

        // TextView
        holder.textView.setText(bikesList.get(position).getCategory());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SingleCategoryActivity.class);
            intent.putExtra("category", bike.getCategory());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bikesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Widget
        private final ImageView cardView;
        private final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.categories_card_image);
            textView = itemView.findViewById(R.id.categories_card_text);
        }
    }
}
