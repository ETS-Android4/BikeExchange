package com.hfad.bikeexchange.screen.menu.discount.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.hfad.bikeexchange.screen.menu.discount.DiscountedItemActivity;

import java.util.ArrayList;

public class DiscountsAdapter extends
        RecyclerView.Adapter<DiscountsAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Item> itemArrayList;

    public DiscountsAdapter(Context context, ArrayList<Item> itemArrayList){
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.discounts_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemArrayList.get(position);

        Glide.with(context).load(item.getImage())
                .into(holder.itemImageView);
        holder.itemNameView.setText(item.getName());
        holder.itemCommonPrice.setText(String.format("$ %s", item.getCommonPrice()));
        holder.itemDiscountPrice.setText(String.format("$ %s", (int)(item.getCommonPrice() * 0.8)));

        holder.itemView.setOnClickListener(v -> {
            Intent discountIntent = new Intent(context, DiscountedItemActivity.class);
            discountIntent.putExtra("id", item.getId());
            context.startActivity(discountIntent);
        });
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView itemImageView;
        private final TextView itemNameView;
        private final TextView itemCommonPrice;
        private final TextView itemDiscountPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            itemImageView = itemView.findViewById(R.id.discounts_item_image);
            itemNameView = itemView.findViewById(R.id.discounts_item_name);
            itemCommonPrice = itemView.findViewById(R.id.common_price);
            itemCommonPrice.setPaintFlags(itemCommonPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemDiscountPrice = itemView.findViewById(R.id.discount_price);
        }
    }
}
