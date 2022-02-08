package com.hfad.bikeexchange.screen.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.hfad.bikeexchange.screen.home.fragment.CategoriesFragment;
import com.hfad.bikeexchange.screen.menu.discount.fragment.DiscountsFragment;
import com.hfad.bikeexchange.screen.menu.message.fragment.MessagesFragment;
import com.hfad.bikeexchange.screen.menu.shop.fragment.MyShopFragment;
import com.hfad.bikeexchange.screen.menu.purchase.PurchasesFragment;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.sign.RegisterActivity;
import com.hfad.bikeexchange.screen.home.fragment.RegisteredUserFragment;
import com.hfad.bikeexchange.screen.cart.fragment.ShoppingCartFragment;
import com.hfad.bikeexchange.screen.home.fragment.UnRegisteredUsersFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseUser firebaseUser = mAuth.getCurrentUser();
    final int SIGN_IN_FRAGMENT = 1;
    final int SIGN_UP_FRAGMENT = 2;

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
    }

    @Override
    public void onStart() {
        super.onStart();

        removeAllFragments();

        if (firebaseUser == null)
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
            fragment = new PurchasesFragment();
        else if (menuItemId == R.id.menu_categories)
            fragment = new CategoriesFragment();
        else if (menuItemId == R.id.menu_discounts)
            fragment = new DiscountsFragment();
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

    public void removeAllFragments() {
        for (Fragment fragment: getSupportFragmentManager().getFragments())
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    public void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .setReorderingAllowed(true)
                //.addToBackStack("home")
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.main_activity_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0)
            super.onBackPressed();
        else
            getSupportFragmentManager().popBackStack();
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

        quitDialog.setPositiveButton("Yes", (dialog, which) ->
                MainActivity.this.finish());

        quitDialog.setNegativeButton("No", (dialog, which) -> {
            Toast.makeText(this, "Great choice! Let's continue shopping!",
                    Toast.LENGTH_SHORT).show();
        });

        quitDialog.show();
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        MenuItem cartItem = menu.findItem(R.id.cart);
        cartItem.setEnabled(firebaseUser != null);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    private void createRegisterActivityIntent(int numberOfFragment) {
        Intent registerActivityIntent = new Intent(this, RegisterActivity.class);
        registerActivityIntent.putExtra("numbOfFragment", numberOfFragment);
        finish();
        startActivity(registerActivityIntent);
    }
}