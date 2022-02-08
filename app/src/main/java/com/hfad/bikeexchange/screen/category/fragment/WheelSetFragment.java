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

public class WheelSetFragment extends Fragment {
    private static final String TAG = "load error";

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ArrayList<Item> wheelSetList;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_categories,
                container,
                false);

        databaseReference = FirebaseDatabase.getInstance().getReference("items");

        recyclerView = view.findViewById(R.id.fragment_categories_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        wheelSetList = new ArrayList<>();

        getWheelsFromFirebase();

        return recyclerView;
    }

    private void getWheelsFromFirebase() {
        databaseReference.orderByChild("category").equalTo("Wheelsets")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot wheelsSnapshot : snapshot.getChildren()) {
                    Item wheelSet = new Item();
                    wheelSet.setId(wheelsSnapshot.child("id").getValue(Integer.class));
                    wheelSet.setImage(wheelsSnapshot.child("image").getValue(String.class));
                    wheelSet.setName(wheelsSnapshot.child("name").getValue(String.class));

                    wheelSetList.add(wheelSet);

                    categoryAdapter = new CategoryAdapter(getContext(), wheelSetList);
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
