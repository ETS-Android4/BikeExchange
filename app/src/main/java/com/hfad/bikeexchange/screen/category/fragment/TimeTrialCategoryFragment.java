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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.models.Item;
import com.hfad.bikeexchange.screen.category.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class TimeTrialCategoryFragment extends Fragment {
    private static final String TAG = "load error";

    private DatabaseReference dbRef;
    private RecyclerView recyclerView;
    private ArrayList<Item> bikesList;
    private CategoryAdapter singleCategoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_categories,
                container, false);

        dbRef = FirebaseDatabase.getInstance().getReference("items");

        recyclerView = view.findViewById(R.id.fragment_categories_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bikesList = new ArrayList<>();

        getBikesFromFirebase();

        return recyclerView;
    }

    public void getBikesFromFirebase() {
        Query timeTrial = dbRef.orderByChild("category").equalTo("Timetrial bikes");

        timeTrial.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Item timeTrialItem = new Item();
                    timeTrialItem.setId(postSnapshot.child("id").getValue(Integer.class));
                    timeTrialItem.setImage(Objects.requireNonNull(postSnapshot.child("image").getValue()).toString());
                    timeTrialItem.setName(Objects.requireNonNull(postSnapshot.child("name").getValue()).toString());
                    bikesList.add(timeTrialItem);

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
