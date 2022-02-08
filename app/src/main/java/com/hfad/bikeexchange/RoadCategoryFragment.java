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
import com.hfad.bikeexchange.adapters.SingleCategoryAdapter;
import com.hfad.bikeexchange.models.Bike;

import java.util.ArrayList;
import java.util.Objects;

public class RoadCategoryFragment extends Fragment {
    private static final String TAG = "load error";

    private RecyclerView recyclerView;
    private DatabaseReference dbRef;
    private ArrayList<Bike> bikesList;
    private SingleCategoryAdapter singleCategoryAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_categories,
                container,
                false);

        dbRef = FirebaseDatabase.getInstance().getReference("bikes");

        recyclerView = view.findViewById(R.id.fragment_categories_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bikesList = new ArrayList<>();

        getBikesFromFirebase();

        return recyclerView;
    }

    private void getBikesFromFirebase(){
        dbRef.orderByChild("category").equalTo("Road bikes")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Bike roadBike = new Bike();
                    roadBike.setImage(Objects.requireNonNull(postSnapshot.child("image").getValue(String.class)));
                    roadBike.setFrame(Objects.requireNonNull(postSnapshot.child("frame").getValue()).toString());
                    roadBike.setPrice(snapshot.child("price").getValue(Integer.class));

                    bikesList.add(roadBike);

                    singleCategoryAdapter = new SingleCategoryAdapter(getContext(), bikesList);
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
