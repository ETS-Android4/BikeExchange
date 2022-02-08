package com.hfad.bikeexchange.screen.category;

import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.category.fragment.HardTailCategoryFragment;
import com.hfad.bikeexchange.screen.category.fragment.PowerMeterFragment;
import com.hfad.bikeexchange.screen.category.fragment.RoadCategoryFragment;
import com.hfad.bikeexchange.screen.category.fragment.SuspensionCategoryFragment;
import com.hfad.bikeexchange.screen.category.fragment.TimeTrialCategoryFragment;
import com.hfad.bikeexchange.screen.category.fragment.WheelSetFragment;

public class CategoryActivity extends AppCompatActivity {
    public CategoryActivity(){
        super(R.layout.activity_single_category);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

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
                setFragment(new HardTailCategoryFragment());
                break;
            case "Road bikes":
                setFragment(new RoadCategoryFragment());
                break;
            case "Suspension bikes":
                setFragment(new SuspensionCategoryFragment());
                break;
            case "Powermeters":
                setFragment(new PowerMeterFragment());
                break;
            case "Wheelsets":
                setFragment(new WheelSetFragment());
                break;
            default:
                setFragment(new TimeTrialCategoryFragment());
                break;
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.single_category_frameLayout, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0)
            super.onBackPressed();
        else
            getSupportFragmentManager().popBackStack();
    }
}