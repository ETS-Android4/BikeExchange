package com.hfad.bikeexchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class BikeDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_detail);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.activity_bike_detail_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.grey));

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view_bike_details);
        navigationView.setNavigationItemSelectedListener(this);

        checkIntent();
    }

    private void checkIntent() {
        String frameNameFromIntent = getIntent().getExtras().getString("frameName");

        if (frameNameFromIntent != null)
            setFragment(sendArgumentsToFragment(frameNameFromIntent));
        else
            setFragment(new ShoppingCartRecycler());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        int menuItemId = menuItem.getItemId();

        if (menuItemId == R.id.menu_nav_home)
            startMainActivity();
        else if (menuItemId == R.id.menu_messages)
            fragment = new MessagesFragment();
        else if (menuItemId == R.id.menu_my_shop)
            fragment = new MyShopFragment();
        else if (menuItemId == R.id.menu_purchases)
            fragment = new PurchaseFragment();
        else if (menuItemId == R.id.menu_categories)
            fragment = new CategoriesFragment();
        else if (menuItemId == R.id.menu_sign_out)
            firebaseAuth.signOut();

        if (fragment != null){
            removeFragment();
            setFragment(fragment);
        }
        else
            setFragment(new BikeDetailsFragment());

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            BikeDetailsFragment bikeDetailsFragment =
                    (BikeDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.bike_details_fragment);
            assert bikeDetailsFragment != null;
            bikeDetailsFragment.myOnKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("bikeDetails")
                .replace(R.id.bike_details_activity_content_frame, fragment).commit();
    }

    private void removeFragment(){
        for (Fragment fragment: getSupportFragmentManager().getFragments())
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    private void startMainActivity(){
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        //mainActivityIntent.putExtra("extras", 1);
        startActivity(mainActivityIntent);
    }

    private Fragment sendArgumentsToFragment(String frameName) {
        Fragment bikeDetailsFragment = new BikeDetailsFragment();
        Bundle args = new Bundle();
        args.putString("frameName", frameName);
        bikeDetailsFragment.setArguments(args);
        return bikeDetailsFragment;
    }
}