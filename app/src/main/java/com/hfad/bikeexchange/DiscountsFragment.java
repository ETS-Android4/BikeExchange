package com.hfad.bikeexchange;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class DiscountsFragment extends Fragment {
    public DiscountsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_discounts, container, false);
    }
}
