package com.hfad.bikeexchange.screen.home.adapter;

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
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.models.Item;
import com.hfad.bikeexchange.screen.category.CategoryActivity;

import java.util.ArrayList;

public class CategoriesAdapter extends
        RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<Item> itemArrayList;

    public CategoriesAdapter(Context mContext, ArrayList<Item> itemArrayList) {
        this.mContext = mContext;
        this.itemArrayList = itemArrayList;
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
        Item item = itemArrayList.get(position);

        //ImageView : Glide Library
        Glide.with(mContext).load(item.getImage())
                .into(holder.cardView);

        // TextView
        holder.textView.setText(item.getCategory());

        holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, CategoryActivity.class);
                intent.putExtra("category", item.getCategory());
                mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
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
