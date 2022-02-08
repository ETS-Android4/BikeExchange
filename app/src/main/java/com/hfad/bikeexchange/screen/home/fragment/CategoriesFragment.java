package com.hfad.bikeexchange.screen.home.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.models.Item;
import com.hfad.bikeexchange.screen.home.adapter.CategoriesAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class CategoriesFragment extends Fragment {
    private static final String TAG = "load error";

    // Widget
    private RecyclerView recyclerView;

    // Firebase
    private DatabaseReference dbRef;

    // Variables
    private ArrayList<Item> itemArrayList;
    private CategoriesAdapter categoriesAdapter;

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
        dbRef = FirebaseDatabase.getInstance().getReference("items");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // ArrayList
        itemArrayList = new ArrayList<>();

        // Clear all
        ClearAll();

        // Get data
        getCategoriesFromFirebase();

        return recyclerView;
    }

    private void getCategoriesFromFirebase() {
        Query queryRoad = dbRef.child("item_9");
        Query queryHard = dbRef.child("item_1");
        Query querySuspension = dbRef.child("item_14");
        Query queryTimetrial = dbRef.child("item_5");
        Query powerMeter = dbRef.child("item_15");
        Query wheelSet = dbRef.child("item_19");

        queryRoad.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Item roadItem = new Item();
                roadItem.setImage(Objects.requireNonNull(dataSnapshot.child("image").getValue(String.class)));
                roadItem.setCategory(Objects.requireNonNull(dataSnapshot.child("category").getValue(String.class)));

                itemArrayList.add(roadItem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost: onCancelled road bikes query", error.toException());
            }
        });

        queryHard.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Item hardItem = new Item();
                hardItem.setImage(Objects.requireNonNull(dataSnapshot.child("image").getValue(String.class)));
                hardItem.setCategory(Objects.requireNonNull(dataSnapshot.child("category").getValue(String.class)));

                itemArrayList.add(hardItem);
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
                Item suspensionItem = new Item();
                suspensionItem.setImage(Objects.requireNonNull(dataSnapshot.child("image").getValue(String.class)));
                suspensionItem.setCategory(Objects.requireNonNull(dataSnapshot.child("category").getValue(String.class)));

                itemArrayList.add(suspensionItem);
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
                Item timetrialItem = new Item();
                timetrialItem.setImage(Objects.requireNonNull(dataSnapshot.child("image").getValue(String.class)));
                timetrialItem.setCategory(Objects.requireNonNull(dataSnapshot.child("category").getValue(String.class)));

                itemArrayList.add(timetrialItem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost: onCancelled", error.toException());
            }
        });

        powerMeter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Item powerMeter = new Item();
                powerMeter.setImage(snapshot.child("image").getValue(String.class));
                powerMeter.setCategory(snapshot.child("category").getValue(String.class));

                itemArrayList.add(powerMeter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost: onCancelled", error.toException());
            }
        });

        wheelSet.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Item wheelSet = new Item();
                wheelSet.setImage(snapshot.child("image").getValue(String.class));
                wheelSet.setCategory(snapshot.child("category").getValue(String.class));

                itemArrayList.add(wheelSet);

                setAdapterForRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost: onCancelled", error.toException());
            }
        });
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void ClearAll() {
        if (itemArrayList != null){
            itemArrayList.clear();

            if (categoriesAdapter != null) {
                categoriesAdapter.notifyDataSetChanged();
            }
        }

        itemArrayList = new ArrayList<>();
    }

    private void setAdapterForRecycler() {
        categoriesAdapter = new CategoriesAdapter(getContext(), itemArrayList);
        recyclerView.setAdapter(categoriesAdapter);
    }
}
