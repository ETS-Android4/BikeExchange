package com.hfad.bikeexchange.screen.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.menu.discount.fragment.DiscountsFragment;
import com.hfad.bikeexchange.screen.menu.shop.fragment.MyShopFragment;

public class RegisteredUserFragment extends Fragment {
    public RegisteredUserFragment() {}

    MaterialButtonToggleGroup materialButtonToggleGroup;
    MaterialButton singleButton;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registered_user, container, false);

        materialButtonToggleGroup = view.findViewById(R.id.registered_toggle_group_button);
        singleButton = view.findViewById(R.id.registered_categories_button);
        int singleButtonId = materialButtonToggleGroup.getCheckedButtonId();
        singleButton = materialButtonToggleGroup.findViewById(singleButtonId);

        materialButtonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked)
                if (checkedId == R.id.registered_categories_button)
                    setFragment(new CategoriesFragment());
                else  if (checkedId == R.id.registered_discount_button)
                    setFragment(new DiscountsFragment());
                else
                    setFragment(new MyShopFragment());

        });

        return view;
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .setReorderingAllowed(true)
                .addToBackStack("home")
                .commit();
    }
}
