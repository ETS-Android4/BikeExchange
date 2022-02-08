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

public class HardTailCategoryFragment extends Fragment {
    private static final String LOAD_ERROR = "load error";
    // Widget
    private RecyclerView recyclerView;

    // Firebase reference
    private DatabaseReference dbRef;

    // Variables
    private ArrayList<Item> bikesList;
    private CategoryAdapter singleCategoryAdapter;

    @Override
    @NonNull
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
        Query hardTailBikes = dbRef.orderByChild("category").equalTo("Hardtail bikes");

        hardTailBikes.addValueEventListener(new ValueEventListener() {
            @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()){
                        Item hardTail = new Item();
                        hardTail.setId(postSnapshot.child("id").getValue(Integer.class));
                        hardTail.setImage(Objects.requireNonNull(postSnapshot.child("image").getValue(String.class)));
                        hardTail.setName(Objects.requireNonNull(postSnapshot.child("name").getValue(String.class)));

                        bikesList.add(hardTail);

                        singleCategoryAdapter = new CategoryAdapter(getContext(), bikesList);
                        recyclerView.setAdapter(singleCategoryAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(LOAD_ERROR, "loadPost: onCancelled", error.toException());
                }
            });
    }
}
