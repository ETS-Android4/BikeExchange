package com.hfad.bikeexchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    int SIGN_IN_FRAGMENT = 1, SIGN_UP_FRAGMENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.main_activity_layout);
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
        signIn.setOnClickListener(v -> createRegisterActivityIntent(SIGN_IN_FRAGMENT));

        Button signUp = headerLay.findViewById(R.id.sign_up_nav_btn);
        signUp.setOnClickListener(v -> createRegisterActivityIntent(SIGN_UP_FRAGMENT));

        //checkIntents();
    }

    // for testing [ it's works ]
    private void checkIntents() {
        String categoryIntent;

        if (getIntent().getExtras() == null)
            categoryIntent = "empty string";
        else {
            categoryIntent = getIntent().getExtras().getString("category");
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
    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        removeAllFragments();

        if (currentUser == null)
            setFragment(new UnRegisteredUsersFragment());
        else
            setFragment(new RegisteredUserFragment());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.cart) {
            Fragment fragment = new ShoppingCartFragment();
            setFragment(fragment);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Fragment fragment = null;
        int menuItemId = menuItem.getItemId();

        if (menuItemId == R.id.menu_nav_home)
            onStart();
        else if (menuItemId == R.id.menu_messages)
            fragment = new MessagesFragment();
        else if (menuItemId == R.id.menu_my_shop)
            fragment = new MyShopFragment();
        else if (menuItemId == R.id.menu_purchases)
            fragment = new PurchaseFragment();
        else if (menuItemId == R.id.menu_categories)
            fragment = new CategoriesFragment();
        else if (menuItemId == R.id.menu_sign_out)
            FirebaseAuth.getInstance().signOut();

        if (fragment != null) {
            removeAllFragments();
            setFragment(fragment);
        } else {
            onStart();
        }

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawer.closeDrawers();

        return true;
    }

    public void removeAllFragments(){
        for (Fragment fragment: getSupportFragmentManager().getFragments())
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            openQuitDialog();
        return false;
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(MainActivity.this);
        quitDialog.setTitle("Do you want to exit from app?");

        quitDialog.setPositiveButton("Yes", (dialog, which) -> MainActivity.this.finish());

        quitDialog.setNegativeButton("No", (dialog, which) -> {
            removeAllFragments();
            onStart();
        });

        quitDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void createRegisterActivityIntent(int numberOfFragment) {
        Intent registerActivityIntent = new Intent(this, RegisterActivity.class);
        registerActivityIntent.putExtra("numbOfFragment", numberOfFragment);
        finish();
        startActivity(registerActivityIntent);
    }
}












