package com.hfad.bikeexchange.screen.menu.purchase.adapter;

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
import com.hfad.bikeexchange.models.Item;

import java.util.ArrayList;

public class PurchasesAdapter extends
        RecyclerView.Adapter<PurchasesAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Item> itemArrayList;

    public PurchasesAdapter(Context context, ArrayList<Item> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.purchases_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemArrayList.get(position);

        Glide.with(context).load(item.getImage())
                .into(holder.purchaseImageView);
        holder.purchaseItemName.setText(item.getName());
        holder.purchaseItemPrice.setText(String.format("$ %s", item.getCommonPrice()));
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView purchaseImageView;
        private final TextView purchaseItemName;
        private final TextView purchaseItemPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            purchaseImageView = itemView.findViewById(R.id.purchases_item_image);
            purchaseItemName = itemView.findViewById(R.id.purchases_item_name);
            purchaseItemPrice = itemView.findViewById(R.id.purchases_item_price);
        }
    }
}
