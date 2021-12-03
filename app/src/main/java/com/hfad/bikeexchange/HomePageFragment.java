package com.hfad.bikeexchange;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.fragment.app.Fragment;

public class HomePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_home_page,
                container,
                false);
    }

    /*@Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Fragment childFragment = new CategoriesFragment();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.child_fragment, childFragment);
    }*/

    @CallSuper
    public void onStart() {
        super.onStart();
    }
    @CallSuper
    public void onStop() {
        super.onStop();
    }
}