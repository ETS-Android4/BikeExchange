package com.hfad.bikeexchange.screen.menu.discount.fragment;

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
import com.hfad.bikeexchange.screen.menu.discount.adapter.DiscountsAdapter;

import java.util.ArrayList;

public class DiscountsRecycler extends Fragment {
    public DiscountsRecycler() {}

    private static final String TAG = "error";

    private RecyclerView recyclerView;
    private DatabaseReference dbRef;
    private ArrayList<Item> itemArrayList;
    private DiscountsAdapter discountsAdapter;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discounts_recycler, container, false);

        recyclerView = view.findViewById(R.id.discounts_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbRef = FirebaseDatabase.getInstance().getReference("items");
        itemArrayList = new ArrayList<>();

        getDiscountedItems();

        return recyclerView;
    }

    private void getDiscountedItems() {
        dbRef.orderByChild("discount").equalTo(true)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            Item discountedItem = new Item();

                            discountedItem.setId(itemSnapshot.child("id").getValue(Integer.class));
                            discountedItem.setImage(itemSnapshot.child("image").getValue(String.class));
                            discountedItem.setName(itemSnapshot.child("name").getValue(String.class));
                            discountedItem.setCommonPrice(itemSnapshot.child("price").getValue(Integer.class));

                            itemArrayList.add(discountedItem);

                            discountsAdapter = new DiscountsAdapter(getContext(), itemArrayList);
                            recyclerView.setAdapter(discountsAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, "loadPost: onCancelled road bikes query", error.toException());
                    }
        });
    }
}
