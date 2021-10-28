package com.hfad.bikeexchange.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.models.Bike;

import java.util.ArrayList;

public class CaptionedImagesAdapter extends
        RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder> {

    private final ArrayList<Bike> bikesList;
    private final Context mContext;

    public CaptionedImagesAdapter(Context mContext, ArrayList<Bike> bikesList) {
        this.mContext = mContext;
        this.bikesList = bikesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TextView
        holder.textView.setText(bikesList.get(position).getFrame());

        //ImageView : Glide Library
        Glide.with(mContext).load(bikesList.get(position).getImage())
                .into(holder.cardView);
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

            cardView = itemView.findViewById(R.id.card_image);
            textView = itemView.findViewById(R.id.card_text);
        }
    }
}
