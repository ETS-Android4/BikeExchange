package com.hfad.bikeexchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RelativeLayout contentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.main_activity_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.grey));

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLay = navigationView.getHeaderView(0);
        Button signIn = headerLay.findViewById(R.id.sign_in_nav_btn);
        signIn.setOnClickListener(v -> createRegisterActivityIntent());

        Button signUp = headerLay.findViewById(R.id.sign_up_nav_btn);
        signUp.setOnClickListener(v -> createRegisterActivityIntent());

        contentFrame = findViewById(R.id.content_frame);
        setFragment(new HomePageFragment());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.search_bar)
            return true;
        else if (id == R.id.cart)
            return true;

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Intent intent = getIntent();

        if (id == R.id.menu_nav_home)
            fragment = new HomePageFragment();
        else if (id == R.id.menu_messages)
            fragment = new MessagesFragment();
        else if (id == R.id.menu_my_shop)
            fragment = new MyShopFragment();
        else if (id == R.id.menu_purchases)
            fragment = new PurchaseFragment();
        else if (id == R.id.menu_categories)
            fragment = new CategoriesFragment();

        if (fragment != null) {
            setFragment(fragment);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        } else {
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.main_activity_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.main_activity_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(contentFrame.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void createRegisterActivityIntent() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        // Fragment fragment = getFragmentManager().getFragment(R.id.si)
        startActivity(registerIntent);
        this.finish();
    }
}












