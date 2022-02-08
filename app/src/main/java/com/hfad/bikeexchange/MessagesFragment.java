package com.hfad.bikeexchange;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hfad.bikeexchange.adapters.SliderFragmentAdapter;

import java.util.ArrayList;

public class MessagesFragment extends Fragment
        implements TabLayoutMediator.TabConfigurationStrategy {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    ArrayList<String> titles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        titles = new ArrayList<>();
        titles.add("Inbox");
        titles.add("Outbox");
        titles.add("Deleted");

        setViewPagerAdapter();
        new TabLayoutMediator(tabLayout, viewPager, this).attach();
        return view;
    }

    public void setViewPagerAdapter(){
        SliderFragmentAdapter sliderFragmentAdapter = new SliderFragmentAdapter(this);
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

        fragmentArrayList.add(new InboxFragment());
        fragmentArrayList.add(new OutboxFragment());
        fragmentArrayList.add(new DeletedFragment());

        sliderFragmentAdapter.setData(fragmentArrayList);
        viewPager.setAdapter(sliderFragmentAdapter);
    }

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position){
        tab.setText(titles.get(position));
    }
}