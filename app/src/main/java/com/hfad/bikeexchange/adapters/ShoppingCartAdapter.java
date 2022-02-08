package com.hfad.bikeexchange.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.models.Bike;

import java.util.ArrayList;

public class ShoppingCartAdapter extends
        RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private final ArrayList<Bike> shoppingCartList;
    private final Context context;
    private final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference("cart list");

    public ShoppingCartAdapter(Context context, ArrayList<Bike> shoppingCartList) {
        this.context = context;
        this.shoppingCartList = shoppingCartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shopping_cart_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bike bike = shoppingCartList.get(position);
        String bikeFrameName = shoppingCartList.get(position).getFrame();

        Glide.with(context).load(shoppingCartList.get(position).getImage())
                .into(holder.bikeImageView);

        holder.bikeFrameName.setText(bikeFrameName);

        holder.price.setText(String.format("$ %s",shoppingCartList.get(position).getPrice()));

        holder.deleteButton.setOnClickListener(v -> {
            cartListRef.child("bikes").orderByChild("frame").equalTo(bikeFrameName)
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot bikeSnapshot : snapshot.getChildren()) {
                        bikeSnapshot.getRef().removeValue();

                        Toast.makeText(context,"Bike has been deleted from shopping cart",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled", error.toException());
                }
            });
            removeBikeFromView(bike);
        });
    }

    public void removeBikeFromView(Bike bike) {
        shoppingCartList.clear();
    }

    @Override
    public int getItemCount() {
        return shoppingCartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView bikeImageView;
        private final TextView bikeFrameName;
        private final TextView price;
        private final Button deleteButton;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            bikeImageView = itemView.findViewById(R.id.shopping_cart_bike_image);
            bikeFrameName = itemView.findViewById(R.id.shopping_cart_bike_frame);
            price = itemView.findViewById(R.id.shopping_cart_bike_price);
            deleteButton = itemView.findViewById(R.id.delete_from_shop_cart_button);
        }
    }
}
