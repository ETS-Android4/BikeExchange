package com.hfad.bikeexchange.screen.cart.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.bikeexchange.R;

import java.util.ArrayList;
import java.util.Objects;

public class ShoppingCartFragment extends Fragment implements View.OnClickListener {
    public ShoppingCartFragment() { }

    private static final String TAG = "error";

    private TextView totalCostView;
    private int totalPayment;
    ArrayList<Integer> itemsList = new ArrayList<>();
    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("cart");
    final String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        totalCostView = view.findViewById(R.id.total_cost);
        Button toPaymentButton = view.findViewById(R.id.payment_button);
        toPaymentButton.setOnClickListener(this);

        getAllItemsFromCart(itemsList);
        calculateTotalCost();

        return view;
    }

    private void getAllItemsFromCart(ArrayList<Integer> itemsList) {
        dbRef.orderByChild("uid").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                            itemsList.add(itemSnapshot.child("id").getValue(Integer.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, "loadPost: onCancelled", error.toException());
                    }
                });
    }

    private void calculateTotalCost() {
        dbRef.orderByChild("uid").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    totalPayment += Objects.requireNonNull(itemSnapshot.child("price").getValue(Integer.class));
                }
                totalCostView.setText(String.format("$ %s", totalPayment));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost: onCancelled", error.toException());
            }
        });
    }

    @Override
    public void onClick(View v) {
        setFragment(totalPayment);
    }

    private void setFragment(int totalPayment) {
        Bundle totalPaymentBundle = new Bundle();
        totalPaymentBundle.putInt("totalPayment", totalPayment);
        totalPaymentBundle.putIntegerArrayList("itemsList", itemsList);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.shopping_cart_content_frame, CheckOutFragment.class, totalPaymentBundle)
                .setReorderingAllowed(true)
                .addToBackStack("item")
                .commit();
    }
}
