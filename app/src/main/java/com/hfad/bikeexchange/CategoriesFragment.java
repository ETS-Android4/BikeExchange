package com.hfad.bikeexchange;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.bikeexchange.adapters.CaptionedImagesAdapter;
import com.hfad.bikeexchange.models.Bike;

import java.util.ArrayList;
import java.util.Objects;

public class CategoriesFragment extends Fragment {

    private static final String TAG = "load error";

    // Widget
    RecyclerView recyclerView;

    // Firebase
    private DatabaseReference dbRef;

    // Variables
    private ArrayList<Bike> bikesList;
    private CaptionedImagesAdapter captionedImagesAdapter;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_categories,
                container,
                false);

        recyclerView = view.findViewById(R.id.fragment_categories_recycler);

        // Firebase
        dbRef = FirebaseDatabase.getInstance().getReference();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // ArrayList
        bikesList = new ArrayList<>();

        // Clear all
        ClearAll();

        // Get data
        getCategoriesFromFirebase();

        return recyclerView;
    }

    private void getCategoriesFromFirebase() {
        Query queryHard = dbRef.child("hardtail").child("obj1");
        Query queryRoad = dbRef.child("road").child("obj5");
        Query querySuspension = dbRef.child("suspension").child("obj10");
        Query queryTimetrial = dbRef.child("timetrial").child("obj13");

        queryRoad.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ClearAll();

                Bike roadBike = new Bike();
                roadBike.setImage(Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString());
                roadBike.setCategory(Objects.requireNonNull(dataSnapshot.child("category").getValue()).toString());

                bikesList.add(roadBike);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost: onCancelled road bikes query", error.toException());
            }
        });

        queryHard.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Bike hardBike = new Bike();
                hardBike.setImage(Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString());
                hardBike.setCategory(Objects.requireNonNull(dataSnapshot.child("category").getValue()).toString());

                bikesList.add(hardBike);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost: onCancelled", error.toException());
            }
        });

        querySuspension.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Bike suspensionBike = new Bike();
                suspensionBike.setImage(Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString());
                suspensionBike.setCategory(Objects.requireNonNull(dataSnapshot.child("category").getValue()).toString());

                bikesList.add(suspensionBike);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost: onCancelled", error.toException());
            }
        });

        queryTimetrial.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Bike timetrialBike = new Bike();
                timetrialBike.setImage(Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString());
                timetrialBike.setCategory(Objects.requireNonNull(dataSnapshot.child("category").getValue()).toString());

                bikesList.add(timetrialBike);

                captionedImagesAdapter = new CaptionedImagesAdapter(getContext(), bikesList);
                recyclerView.setAdapter(captionedImagesAdapter);
                //captionedImagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost: onCancelled", error.toException());
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void ClearAll() {
        if (bikesList != null){
            bikesList.clear();

            if (captionedImagesAdapter != null) {
                captionedImagesAdapter.notifyDataSetChanged();
            }
        }

        bikesList = new ArrayList<>();
    }
}
