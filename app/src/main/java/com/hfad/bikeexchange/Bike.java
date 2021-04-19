package com.hfad.bikeexchange;

public class Bike {
    private String name;
    private int imageResourceId;

    public static final Bike[] bikes = {
            new Bike("Bianchi Oltre XR4", R.drawable.bianchi_oltre_xr4),
            new Bike("Trek Madone", R.drawable.trek_madone)
    };

    private Bike(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
