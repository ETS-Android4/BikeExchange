package com.hfad.bikeexchange.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hfad.bikeexchange.BikeDetailsActivity;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.models.Bike;

import java.util.ArrayList;

public class SingleCategoryAdapter extends
        RecyclerView.Adapter<SingleCategoryAdapter.ViewHolder> {

    private final ArrayList<Bike> bikesList;
    private final Context context;

    public SingleCategoryAdapter(Context context, ArrayList<Bike> bikesList) {
        this.bikesList = bikesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.categories_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bike bike = bikesList.get(position);

        // Text view
        holder.bikeNameView.setText(bikesList.get(position).getFrame());

        // Image view : Glide library
        Glide.with(context).load(bikesList.get(position).getImage())
                .into(holder.bikeImageView);

        holder.itemView.setOnClickListener(v -> {
            Intent bikeDetailsIntent = new Intent(context, BikeDetailsActivity.class);
            bikeDetailsIntent.putExtra("frameName", bike.getFrame());
            context.startActivity(bikeDetailsIntent);
        });
    }

    @Override
    public int getItemCount() { return bikesList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Widget
        private final ImageView bikeImageView;
        private final TextView bikeNameView;

        public ViewHolder(View itemView){
            super(itemView);

            bikeImageView = itemView.findViewById(R.id.categories_card_image);
            bikeNameView = itemView.findViewById(R.id.categories_card_text);
        }
    }
}
