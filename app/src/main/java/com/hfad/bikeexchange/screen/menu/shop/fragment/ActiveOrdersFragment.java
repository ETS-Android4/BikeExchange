package com.hfad.bikeexchange.screen.menu.shop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.menu.shop.adapter.TrackingAdapter;
import com.hfad.bikeexchange.models.Item;

import java.util.ArrayList;

public class ActiveOrdersFragment extends Fragment {

    RecyclerView recyclerView;
    private ArrayList<Item> activeOrdersList;
    private TrackingAdapter trackingAdapter;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("active orders");

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_orders, container, false);

        recyclerView = view.findViewById(R.id.active_orders_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        activeOrdersList = new ArrayList<>();

        return view;
    }
}
