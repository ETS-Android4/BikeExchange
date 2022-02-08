package com.hfad.bikeexchange;

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
import com.hfad.bikeexchange.adapters.ShoppingCartAdapter;
import com.hfad.bikeexchange.models.Bike;

import java.util.ArrayList;
import java.util.Objects;

public class ShoppingCartRecycler extends Fragment {
    private static final String TAG = "load error";

    private RecyclerView recyclerView;
    private ArrayList<Bike> bikesList;
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

        shoppingCartList = FirebaseDatabase.getInstance().getReference("cart list");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bikesList = new ArrayList<>();

        getBikesFromFirebase();

        return recyclerView;
    }

    private void getBikesFromFirebase() {
        shoppingCartList.child("bikes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot bikeSnapshot : snapshot.getChildren()) {
                            Bike bike = new Bike();

                            bike.setImage(Objects.requireNonNull(bikeSnapshot.child("image").getValue(String.class)));
                            bike.setFrame(Objects.requireNonNull(bikeSnapshot.child("frame").getValue(String.class)));
                            bike.setPrice(bikeSnapshot.child("price").getValue(Integer.class));

                            bikesList.add(bike);

                            shoppingCartAdapter = new ShoppingCartAdapter(getContext(), bikesList);
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
