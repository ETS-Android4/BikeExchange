package com.hfad.bikeexchange.screen.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.hfad.bikeexchange.screen.menu.discount.fragment.DiscountsFragment;
import com.hfad.bikeexchange.screen.menu.shop.fragment.MyShopFragment;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.sign.RegisterActivity;

public class UnRegisteredUsersFragment extends Fragment {
    public UnRegisteredUsersFragment() { }

    MaterialButtonToggleGroup topPartButtonsGroup, signInUpButtonsGroup;
    MaterialButton topPartButton, signInUpButton;
    final int SIGN_IN_FRAGMENT = 1, SIGN_UP_FRAGMENT = 2;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_unregistered_users, container, false);

        topPartButtonsGroup = view.findViewById(R.id.unregistered_toggle_group_button);
        topPartButton = view.findViewById(R.id.categories_button);
        int topPartButtonId = topPartButtonsGroup.getCheckedButtonId();
        topPartButton = topPartButtonsGroup.findViewById(topPartButtonId);

        topPartButtonsGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if(isChecked)
                if(checkedId == R.id.categories_button)
                    setFragment(new CategoriesFragment());
                else if(checkedId == R.id.discount_button)
                    setFragment(new DiscountsFragment());
                else
                    setFragment(new MyShopFragment());
        });

        signInUpButtonsGroup = view.findViewById(R.id.req_auth_sign_in_up_group);
        signInUpButton = view.findViewById(R.id.sign_in_req_auth);
        int reqAuthButton = signInUpButtonsGroup.getCheckedButtonId();
        signInUpButton = signInUpButtonsGroup.findViewById(reqAuthButton);

        signInUpButtonsGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if(isChecked)
                if (checkedId == R.id.sign_in_req_auth)
                    startRegisterActivityFragment(SIGN_IN_FRAGMENT);
                else
                    startRegisterActivityFragment(SIGN_UP_FRAGMENT);
        });

        return view;
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .setReorderingAllowed(true)
                .addToBackStack("home")
                .commit();
    }

    public void startRegisterActivityFragment(int numbOfFragment) {
        Intent registerActivityIntent = new Intent(getActivity(), RegisterActivity.class);
        registerActivityIntent.putExtra("numbOfFragment", numbOfFragment);
        startActivity(registerActivityIntent);
    }
}