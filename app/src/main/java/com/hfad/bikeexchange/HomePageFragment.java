package com.hfad.bikeexchange;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfad.bikeexchange.adapters.CaptionedImagesAdapter;
import com.hfad.bikeexchange.models.Bike;

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

        adapter.setListener(new CaptionedImagesAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), BikeDetailActivity.class);
                intent.putExtra(BikeDetailActivity.EXTRA_BIKE_ID, position);
                getActivity().startActivity(intent);
            }
        });

        return bikeRecycler;
    }
}