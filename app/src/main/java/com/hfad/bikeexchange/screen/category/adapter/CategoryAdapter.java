package com.hfad.bikeexchange.screen.category.adapter;

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
import com.hfad.bikeexchange.models.Item;
import com.hfad.bikeexchange.screen.item.ItemDetailsActivity;
import com.hfad.bikeexchange.R;

import java.util.ArrayList;

public class CategoryAdapter extends
        RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Item> itemArrayList;

    public CategoryAdapter(Context context, ArrayList<Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
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
        Item item = itemArrayList.get(position);

        // Image view : Glide library
        Glide.with(context).load(item.getImage())
                .into(holder.itemImageView);

        // Text view
        holder.itemNameView.setText(item.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent bikeDetailsIntent = new Intent(context, ItemDetailsActivity.class);
            bikeDetailsIntent.putExtra("id", item.getId());
            context.startActivity(bikeDetailsIntent);
        });
    }

    @Override
    public int getItemCount() { return itemArrayList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Widget
        private final ImageView itemImageView;
        private final TextView itemNameView;

        public ViewHolder(View itemView){
            super(itemView);

            itemImageView = itemView.findViewById(R.id.categories_card_image);
            itemNameView = itemView.findViewById(R.id.categories_card_text);
        }
    }
}
