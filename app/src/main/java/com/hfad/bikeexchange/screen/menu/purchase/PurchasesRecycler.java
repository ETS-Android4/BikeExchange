package com.hfad.bikeexchange.screen.menu.purchase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.models.Item;
import com.hfad.bikeexchange.screen.menu.purchase.adapter.PurchasesAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class PurchasesRecycler extends Fragment {
    private static final String TAG = "error";

    public PurchasesRecycler() { }

    private RecyclerView recyclerView;
    private DatabaseReference purchasesDbRef, allItemsDbRef;
    private PurchasesAdapter purchasesAdapter;
    ArrayList<Integer> itemIds = new ArrayList<>();
    ArrayList<Item> itemArrayList = new ArrayList<>();

    @NonNull
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.purchases_recycler, container, false);

        recyclerView = view.findViewById(R.id.purchases_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        purchasesDbRef = FirebaseDatabase.getInstance().getReference("purchases");
        allItemsDbRef = FirebaseDatabase.getInstance().getReference("items");

        getPurchases();

        return view;
    }

    private void getPurchases() {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        purchasesDbRef.orderByChild("userId").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot purchaseSnapshot : snapshot.getChildren()) {
                    itemIds.add(purchaseSnapshot.child("itemId").getValue(Integer.class));
                }
                setViewComponents();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost: onCancelled", error.toException());
            }
        });
    }

    private void setViewComponents() {
        for (int i : itemIds) {
            allItemsDbRef.orderByChild("id").equalTo(i)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                                Item item = new Item();

                                item.setImage(itemSnapshot.child("image").getValue(String.class));
                                item.setName(itemSnapshot.child("name").getValue(String.class));
                                item.setCommonPrice(itemSnapshot.child("price").getValue(Integer.class));

                                itemArrayList.add(item);

                                purchasesAdapter = new PurchasesAdapter(getContext(), itemArrayList);
                                recyclerView.setAdapter(purchasesAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.w(TAG, "loadPost: onCancelled", error.toException());
                        }
                    });
        }
    }
}
