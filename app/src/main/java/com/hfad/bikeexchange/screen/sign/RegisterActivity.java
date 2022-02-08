package com.hfad.bikeexchange.screen.sign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.home.MainActivity;
import com.hfad.bikeexchange.screen.sign.fragment.SignInFragment;
import com.hfad.bikeexchange.screen.sign.fragment.SignUpFragment;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getIntent().getExtras().getInt("numbOfFragment") == 1)
            setFragment(new SignInFragment());
        else
            setFragment(new SignUpFragment());
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.register_frameLayout, fragment)
                .setReorderingAllowed(true)
                //.addToBackStack("sign")
                .commit();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 1) {
            startMainActivity();
            finish();
            //super.onBackPressed();
        }
        else
            getSupportFragmentManager().popBackStack();
    }

    private void startMainActivity(){
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        finish();
        startActivity(mainActivityIntent);
    }
}