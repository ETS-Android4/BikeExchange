package com.hfad.bikeexchange.screen.category.fragment;

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
import com.hfad.bikeexchange.screen.category.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class RoadCategoryFragment extends Fragment {
    private static final String TAG = "load error";

    private RecyclerView recyclerView;
    private DatabaseReference dbRef;
    private ArrayList<Item> bikesList;
    private CategoryAdapter singleCategoryAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories,
                container,
                false);

        dbRef = FirebaseDatabase.getInstance().getReference("items");

        recyclerView = view.findViewById(R.id.fragment_categories_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bikesList = new ArrayList<>();

        getBikesFromFirebase();

        return recyclerView;
    }

    private void getBikesFromFirebase() {
        dbRef.orderByChild("category").equalTo("Road bikes")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot bikeSnapshot: snapshot.getChildren()) {
                    Item bikeItem = new Item();
                    bikeItem.setId(bikeSnapshot.child("id").getValue(Integer.class));
                    bikeItem.setImage(Objects.requireNonNull(bikeSnapshot.child("image").getValue(String.class)));
                    bikeItem.setName(Objects.requireNonNull(bikeSnapshot.child("name").getValue(String.class)));

                    bikesList.add(bikeItem);

                    singleCategoryAdapter = new CategoryAdapter(getContext(), bikesList);
                    recyclerView.setAdapter(singleCategoryAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost: onCancelled", error.toException());
            }
        });
    }
}
