package com.hfad.bikeexchange.screen.cart.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.models.Item;
import com.hfad.bikeexchange.screen.cart.adapter.ShoppingCartAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class ShoppingCartRecycler extends Fragment {
    private static final String TAG = "load error";

    private RecyclerView recyclerView;
    private ArrayList<Item> itemArrayList;
    private DatabaseReference shoppingCartList;
    private ShoppingCartAdapter shoppingCartAdapter;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.shopping_cart_recycler,
                container,
                false);

        recyclerView = view.findViewById(R.id.shopping_cart_recycle_view);

        shoppingCartList = FirebaseDatabase.getInstance().getReference("cart");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemArrayList = new ArrayList<>();

        getBikesFromFirebase();

        return recyclerView;
    }

    private void getBikesFromFirebase() {
        shoppingCartList.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot bikeSnapshot : snapshot.getChildren()) {
                            Item item = new Item();

                            item.setImage(Objects.requireNonNull(bikeSnapshot.child("image").getValue(String.class)));
                            item.setName(Objects.requireNonNull(bikeSnapshot.child("name").getValue(String.class)));
                            item.setCommonPrice(bikeSnapshot.child("price").getValue(Integer.class));

                            itemArrayList.add(item);

                            shoppingCartAdapter = new ShoppingCartAdapter(getContext(), itemArrayList);
                            recyclerView.setAdapter(shoppingCartAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, "loadPost: onCancelled", error.toException());
                    }
                });
    }
}
