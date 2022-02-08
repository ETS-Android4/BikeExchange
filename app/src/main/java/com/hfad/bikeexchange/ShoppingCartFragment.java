package com.hfad.bikeexchange;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ShoppingCartFragment extends Fragment {
    public ShoppingCartFragment() { }

    private static final String TAG = "error";
    private static final String FRAGMENT_TAG = "ShoppingCart fragment";

    private TextView totalCostView;
    private final DatabaseReference shoppingCartRef = FirebaseDatabase.getInstance().getReference("cart list");

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        totalCostView = view.findViewById(R.id.total_cost);

        getTotalCost();

        return view;
    }

    private void getTotalCost() {
        shoppingCartRef.child("bikes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalPayment = 0;
                for (DataSnapshot shopCartSnap : snapshot.getChildren()) {
                    totalPayment += Objects.requireNonNull(shopCartSnap.child("price").getValue(Integer.class));
                }
                totalCostView.setText(String.format("$ %s", totalPayment));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost: onCancelled", error.toException());
            }
        });
    }
}
