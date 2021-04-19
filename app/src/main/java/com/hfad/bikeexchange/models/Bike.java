package com.hfad.bikeexchange.models;

import com.hfad.bikeexchange.R;

public class Bike {
    private String name;
    private String category;
    private int imageResourceId;

    public static final Bike[] bikes = {
            new Bike("Bianchi Oltre XR4", "road", R.drawable.bianchi_oltre_xr4),
            new Bike("Trek Madone", "road", R.drawable.trek_madone),
            new Bike("Cannondale SS Hi-Mod", "road", R.drawable.cannondale_supersix_himod),
            new Bike("Trek Emonda", "road", R.drawable.trek_emonda),
            new Bike("Pinarello Dogma F12", "road", R.drawable.pinarello_dogma_f12),
            new Bike("Felt", "road", R.drawable.felt)
    };

    private Bike(String name, String category, int imageResourceId) {
        this.name = name;
        this.category = category;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }
    public String getCategory() { return category; }
    public int getImageResourceId() {
        return imageResourceId;
    }
}
