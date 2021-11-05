package com.hfad.bikeexchange;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.fragment.app.Fragment;

public class HomePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_home_page,
                container,
                false);
    }

    @CallSuper
    public void onStart() {
        super.onStart();
    }
    @CallSuper
    public void onStop() {
        super.onStop();
    }
}