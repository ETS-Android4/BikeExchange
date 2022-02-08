package com.hfad.bikeexchange.screen.menu.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.models.Item;

import java.util.ArrayList;

public class TrackingAdapter extends
        RecyclerView.Adapter<TrackingAdapter.ViewHolder> {

    private final ArrayList<Item> trackingCartList;
    private final Context context;
    private final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference();

    public TrackingAdapter(Context context, ArrayList<Item> shoppingCartList) {
        this.context = context;
        this.trackingCartList = shoppingCartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tracking_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackingAdapter.ViewHolder holder, int position) {
        Item item = trackingCartList.get(position);
        String bikeFrameName = trackingCartList.get(position).getName();

        Glide.with(context).load(trackingCartList.get(position).getImage())
                .into(holder.bikeImageView);

        holder.bikeFrameName.setText(bikeFrameName);

        holder.price.setText(String.format("$ %s",trackingCartList.get(position).getCommonPrice()));
    }

    @Override
    public int getItemCount() {
        return trackingCartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView bikeImageView;
        private final TextView bikeFrameName;
        private final TextView price;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            bikeImageView = itemView.findViewById(R.id.active_orders_card_image);
            bikeFrameName = itemView.findViewById(R.id.active_orders_card_frame_name);
            price = itemView.findViewById(R.id.active_orders_card_price);
        }
    }
}
