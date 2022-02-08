package com.hfad.bikeexchange.screen.cart.adapter;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.models.Item;

import java.util.ArrayList;
import java.util.Objects;

public class ShoppingCartAdapter extends
        RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private final ArrayList<Item> shoppingCartList;
    private final Context context;
    private final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference("cart");
    private final String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    public ShoppingCartAdapter(Context context, ArrayList<Item> shoppingCartList) {
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
        Item item = shoppingCartList.get(position);
        String itemName = item.getName();

        Glide.with(context).load(shoppingCartList.get(position).getImage())
                .into(holder.itemImageView);

        holder.itemNameView.setText(itemName);

        holder.itemPriceView.setText(String.format("$ %s",shoppingCartList.get(position).getCommonPrice()));

        holder.deleteButton.setOnClickListener(v -> {
            cartListRef.orderByChild("name").equalTo(itemName)
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot bikeSnapshot : snapshot.getChildren()) {
                        bikeSnapshot.getRef().removeValue();

                        Toast.makeText(context,"Item has been deleted from shopping cart",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled", error.toException());
                }
            });

            shoppingCartList.clear();
        });
    }

    @Override
    public int getItemCount() {
        return shoppingCartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView itemImageView;
        private final TextView itemNameView;
        private final TextView itemPriceView;
        private final Button deleteButton;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            itemImageView = itemView.findViewById(R.id.shopping_cart_bike_image);
            itemNameView = itemView.findViewById(R.id.shopping_cart_bike_frame);
            itemPriceView = itemView.findViewById(R.id.shopping_cart_bike_price);
            deleteButton = itemView.findViewById(R.id.delete_from_shop_cart_button);
        }
    }
}
