package com.hfad.bikeexchange.screen.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.MobileAds;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.home.MainActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        MobileAds.initialize(this, initializationStatus -> {
        });

        SystemClock.sleep(200);
        Intent registerIntent = new Intent(this, MainActivity.class);
        startActivity(registerIntent);
        finish();
    }
}