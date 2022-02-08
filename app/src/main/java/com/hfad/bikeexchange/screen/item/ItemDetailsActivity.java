package com.hfad.bikeexchange.screen.item;

import android.content.Intent;
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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.hfad.bikeexchange.screen.item.fragment.ItemDetailsFragment;
import com.hfad.bikeexchange.screen.menu.discount.fragment.DiscountsFragment;
import com.hfad.bikeexchange.screen.menu.message.fragment.MessagesFragment;
import com.hfad.bikeexchange.screen.menu.shop.fragment.MyShopFragment;
import com.hfad.bikeexchange.screen.menu.purchase.PurchasesFragment;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.home.MainActivity;
import com.hfad.bikeexchange.screen.home.fragment.CategoriesFragment;

public class ItemDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_details);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.activity_item_details_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.nav_close_drawer,
                R.string.nav_open_drawer);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.grey));

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view_item_details);
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        checkIntent();
    }

    private void checkIntent() {
        int itemId = getIntent().getExtras().getInt("id");

        if (itemId != 0)
            setFragment(sendArgumentsToFragment(itemId));
        else
            startMainActivity();    // &&&&&????????????????????????
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
            fragment = new PurchasesFragment();
        else if (menuItemId == R.id.menu_categories)
            fragment = new CategoriesFragment();
        else if (menuItemId == R.id.menu_discounts)
            fragment = new DiscountsFragment();
        else if (menuItemId == R.id.menu_sign_out)
            firebaseAuth.signOut();

        if (fragment != null){
            removeFragment();
            setFragment(fragment);
        }
        else
            setFragment(new ItemDetailsFragment());

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();

        return true;
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0)
            super.onBackPressed();
        else
            getSupportFragmentManager().popBackStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.item_details_activity_content_frame, fragment)
                .setReorderingAllowed(true)
                //.addToBackStack("home")
                .commit();
    }

    private void removeFragment(){
        for (Fragment fragment: getSupportFragmentManager().getFragments())
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    private void startMainActivity(){
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        finish();
        startActivity(mainActivityIntent);
    }

    private Fragment sendArgumentsToFragment(int itemId) {
        Fragment itemDetailsFragment = new ItemDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("id", itemId);
        itemDetailsFragment.setArguments(args);
        return itemDetailsFragment;
    }
}