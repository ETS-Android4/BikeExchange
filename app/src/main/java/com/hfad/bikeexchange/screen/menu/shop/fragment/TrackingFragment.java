package com.hfad.bikeexchange.screen.menu.shop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hfad.bikeexchange.R;
import com.hfad.bikeexchange.screen.menu.message.adapter.SliderFragmentAdapter;

import java.util.ArrayList;

public class TrackingFragment extends Fragment
        implements TabLayoutMediator.TabConfigurationStrategy {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ArrayList<String> titles;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tracking, container, false);

        tabLayout = view.findViewById(R.id.tracking_fragment_tabLayout);
        viewPager2 = view.findViewById(R.id.tracking_fragment_viewPager);
        titles = new ArrayList<>();
        titles.add("Active");
        titles.add("Completed");

        setViewPagerAdapter();
        new TabLayoutMediator(tabLayout, viewPager2, this).attach();

        return view;
    }

    public void setViewPagerAdapter() {
        SliderFragmentAdapter sliderFragmentAdapter = new SliderFragmentAdapter(this);
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

        fragmentArrayList.add(new ActiveOrdersFragment());
        fragmentArrayList.add(new CompletedOrdersFragment());

        sliderFragmentAdapter.setData(fragmentArrayList);
        viewPager2.setAdapter(sliderFragmentAdapter);
    }

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(titles.get(position));
    }
}
