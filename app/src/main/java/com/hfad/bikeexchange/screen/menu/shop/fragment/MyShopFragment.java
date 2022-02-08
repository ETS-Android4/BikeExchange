package com.hfad.bikeexchange.screen.menu.shop.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.hfad.bikeexchange.screen.cart.fragment.CheckOutFragment;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.home.fragment.CategoriesFragment;
import com.hfad.bikeexchange.screen.menu.shop.fragment.TrackingFragment;

public class MyShopFragment extends Fragment {

    ImageButton closeButton;
    MaterialButtonToggleGroup myShopToggleGroup;
    MaterialButton materialTrackingButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_shop, container, false);

        myShopToggleGroup = view.findViewById(R.id.my_shop_material_button_toggle);
        materialTrackingButton = view.findViewById(R.id.tracking_button);
        int materialButtonId = myShopToggleGroup.getCheckedButtonId();
        materialTrackingButton = myShopToggleGroup.findViewById(materialButtonId);

        myShopToggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked)
                    if (checkedId == R.id.tracking_button)
                        setFragment(new TrackingFragment());
                    else if (checkedId == R.id.saved_button)
                        setFragment(new CategoriesFragment());
                    else
                        setFragment(new CheckOutFragment());        // not correct
            }
        });

        closeButton = view.findViewById(R.id.my_shop_close_button);

        return view;
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.my_shop_container, fragment).commit();
    }
}