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

import com.google.firebase.auth.FirebaseAuth;
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

public class PowerMeterFragment extends Fragment {
    private static final String TAG = "load error";

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private String uid;
    private ArrayList<Item> powerMeterList;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_categories,
                container,
                false);

        databaseReference = FirebaseDatabase.getInstance().getReference("items");
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        recyclerView = view.findViewById(R.id.fragment_categories_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        powerMeterList = new ArrayList<>();

        getPowerMetersFromFirebase();

        return recyclerView;
    }

    private void getPowerMetersFromFirebase() {
        databaseReference.orderByChild("category").equalTo("Powermeters")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot powerMeterShot : snapshot.getChildren()) {
                    Item powerMeter = new Item();
                    powerMeter.setId(powerMeterShot.child("id").getValue(Integer.class));
                    powerMeter.setImage(powerMeterShot.child("image").getValue(String.class));
                    powerMeter.setName(powerMeterShot.child("name").getValue(String.class));

                    powerMeterList.add(powerMeter);

                    categoryAdapter = new CategoryAdapter(getContext(), powerMeterList);
                    recyclerView.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost: onCancelled", error.toException());
            }
        });
    }
}