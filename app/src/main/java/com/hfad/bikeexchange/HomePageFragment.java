package com.hfad.bikeexchange;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView bikeRecycler = (RecyclerView) inflater.inflate(
                R.layout.fragment_home_page,
                container,
                false);

        String[] bikeNames = new String[Bike.bikes.length];
        for (int i = 0; i < bikeNames.length; i++) {
            bikeNames[i] = Bike.bikes[i].getName();
        }

        int[] bikeImages = new int[Bike.bikes.length];
        for (int i = 0; i < bikeImages.length; i++) {
            bikeImages[i] = Bike.bikes[i].getImageResourceId();
        }

        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(bikeNames, bikeImages);
        bikeRecycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        bikeRecycler.setLayoutManager(layoutManager);
        return bikeRecycler;
    }
}