package com.hfad.bikeexchange;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class SingleCategoryActivity extends AppCompatActivity {
    public SingleCategoryActivity(){
        super(R.layout.activity_single_category);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_single_category);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.single_category_activity_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.grey));

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        String categoryIntent = getIntent().getExtras().getString("category");

        switch (categoryIntent) {
            case "Hardtail bikes":
                setFragment(new HardtailCategoryFragment());
                break;
            case "Road bikes":
                setFragment(new RoadCategoryFragment());
                break;
            case "Suspension bikes":
                setFragment(new SuspensionCategoryFragment());
                break;
            default:
                setFragment(new TimetrialCategoryFragment());
                break;
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.single_category_frameLayout, fragment).commit();
    }
}