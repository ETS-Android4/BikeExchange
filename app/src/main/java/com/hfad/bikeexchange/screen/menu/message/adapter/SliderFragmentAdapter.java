package com.hfad.bikeexchange.screen.menu.message.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;

public class SliderFragmentAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragments;

    public SliderFragmentAdapter(Fragment fragment){
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public void setData(ArrayList<Fragment> fragments) {
        this.fragments = fragments;
    }
}
