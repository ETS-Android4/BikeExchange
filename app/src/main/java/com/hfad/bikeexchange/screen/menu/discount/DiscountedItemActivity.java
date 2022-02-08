package com.hfad.bikeexchange.screen.menu.discount;

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
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.home.MainActivity;
import com.hfad.bikeexchange.screen.home.fragment.CategoriesFragment;
import com.hfad.bikeexchange.screen.menu.discount.fragment.DiscountedItemFragment;
import com.hfad.bikeexchange.screen.menu.discount.fragment.DiscountsFragment;
import com.hfad.bikeexchange.screen.menu.message.fragment.MessagesFragment;
import com.hfad.bikeexchange.screen.menu.purchase.PurchasesFragment;
import com.hfad.bikeexchange.screen.menu.shop.fragment.MyShopFragment;

public class DiscountedItemActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discounted_item);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.discounted_item_activity_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.nav_open_drawer,
                R.string.nav_close_drawer);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.grey));

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view_discounted_item);
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();

        checkIntent();
    }

    private void checkIntent() {
        int itemId = getIntent().getExtras().getInt("id");

        if (itemId != 0)
            setFragment(sendArgumentsToFragment(itemId));
        else
            startMainActivity();        // ???????????????????
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        displayView(menuItem.getItemId());

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();

        return true;
    }

    private void displayView(int menuItemId) {
        Fragment fragment = new Fragment();

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

        checkFragment(fragment);
    }

    private void checkFragment(Fragment fragment) {
        if (fragment != null) {
            removeFragment();
            setFragment(fragment);
        }
        else
            setFragment(new DiscountsFragment());
    }

    private void removeFragment() {
        for (Fragment fragment : getSupportFragmentManager().getFragments())
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.discounted_item_activity_content_frame, fragment)
                .commit();
    }

    private Fragment sendArgumentsToFragment(int itemId) {
        Fragment discountedItemDetailsFragment = new DiscountedItemFragment();
        Bundle args = new Bundle();
        args.putInt("id", itemId);
        discountedItemDetailsFragment.setArguments(args);
        return discountedItemDetailsFragment;
    }

    private void startMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        finish();
        startActivity(mainActivityIntent);
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
}