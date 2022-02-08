package com.hfad.bikeexchange.screen.cart.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fevziomurtekin.payview.Payview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.home.MainActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class PaymentFragment extends Fragment {
    public PaymentFragment() {}

    private static final String TAG = "error";

    String orderKey;
    int totalPayment;
    TextView totalCostView;
    Payview payview;
    DatabaseReference dbRef;
    FirebaseAuth firebaseAuth;

    @Override
    public View  onCreateView(LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        totalCostView = view.findViewById(R.id.total_cost_payment);
        payview = view.findViewById(R.id.payment_view);

        payview.setPayOnclickListener(v -> {
            updateOrder();
            clearCart();
        });

        dbRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    private void clearCart() {
        String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        dbRef.child("cart").orderByChild("uid").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                    cartSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost: onCancelled", error.toException());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        totalPayment = requireArguments().getInt("totalPayment");

        totalCostView.setText(String.format("$ %s", totalPayment));

        payview.setOnDataChangedListener((payModel, isFillAllComponents) -> {

        });
    }

    private void updateOrder() {
        orderKey = requireArguments().getString("orderKey");
        Date currentTime = new Date();
        String dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).format(currentTime);

        HashMap<String, Object> orderHashMap = new HashMap<>();
        orderHashMap.put("totalCost", totalPayment);
        orderHashMap.put("date", dateFormat);

        dbRef.child("orders").child(orderKey).updateChildren(orderHashMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(),
                                "Your payment will be processed as soon as possible.",
                                Toast.LENGTH_SHORT).show();
                        SystemClock.sleep(2000);
                        startMainActivity();
                    }
                    else
                        Toast.makeText(getActivity(),
                                "Something went wrong.",
                                Toast.LENGTH_SHORT).show();
                });
    }
    private void startMainActivity() {
        Intent mainActivityIntent = new Intent(getContext(), MainActivity.class);
        requireActivity().finish();
        requireActivity().startActivity(mainActivityIntent);
    }
}



