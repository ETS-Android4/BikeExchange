package com.hfad.bikeexchange;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BikeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_BIKE_ID = "bikeId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        int bikeId = (Integer) getIntent().getExtras().get(EXTRA_BIKE_ID);
        String bikeName = Bike.bikes[bikeId].getName();
        TextView textView = (TextView) findViewById(R.id.bike_text);
        textView.setText(bikeName);

        int bikeImage = Bike.bikes[bikeId].getImageResourceId();
        ImageView imageView = (ImageView)findViewById(R.id.bike_image);
        imageView.setImageDrawable(ContextCompat.getDrawable(this, bikeImage));
        imageView.setContentDescription(bikeName);
    }
}