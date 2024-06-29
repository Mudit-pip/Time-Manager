package com.timemanagerweek.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.timemanagerweek.R;
import com.timemanagerweek.VPadapter;


public class fragment_list extends Fragment {

    public fragment_list() {
    }


    TabLayout tab;
    ViewPager vp;
    int position = 0;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        tab = v.findViewById(R.id.frag_list_tab_lay);
        vp = v.findViewById(R.id.frag_list_viewpager);

        VPadapter adapter = new VPadapter(getChildFragmentManager());
        vp.setAdapter(adapter);


        SharedPreferences pref = getActivity().getSharedPreferences("pref_Mudit", getActivity().MODE_PRIVATE);
        int frag_pos = pref.getInt("frag_pos", 0);

        if (frag_pos == 0) {
            vp.setCurrentItem(0);
        } else if (frag_pos == 1) {
            vp.setCurrentItem(1);
        } else if (frag_pos == 2) {
            vp.setCurrentItem(2);
        } else if (frag_pos == 3) {
            vp.setCurrentItem(3);
        } else if (frag_pos == 4) {
            vp.setCurrentItem(4);
        } else if (frag_pos == 5) {
            vp.setCurrentItem(5);
        } else if (frag_pos == 6) {
            vp.setCurrentItem(6);
        }

        tab.setupWithViewPager(vp);

        int colour = pref.getInt("theme", 0);
        int black = ContextCompat.getColor(getContext(), R.color.black); // Get color from colors.xml
        if(colour == 1){
            int tabTextColor = ContextCompat.getColor(getContext(), R.color.custom_color1); // Get color from colors.xml
            tab.setSelectedTabIndicatorColor(tabTextColor);
            tab.setTabTextColors(black, tabTextColor);
        } else if(colour == 2){
            int tabTextColor = ContextCompat.getColor(getContext(), R.color.custom_color2); // Get color from colors.xml
            tab.setSelectedTabIndicatorColor(tabTextColor);
            tab.setTabTextColors(black, tabTextColor);
        } else if(colour == 3){
            int tabTextColor = ContextCompat.getColor(getContext(), R.color.custom_color3); // Get color from colors.xml
            tab.setSelectedTabIndicatorColor(tabTextColor);
            tab.setTabTextColors(black, tabTextColor);
        } else if(colour == 4){
            int tabTextColor = ContextCompat.getColor(getContext(), R.color.custom_color4); // Get color from colors.xml
            tab.setSelectedTabIndicatorColor(tabTextColor);
            tab.setTabTextColors(black, tabTextColor);
        } else if(colour == 5){
            int tabTextColor = ContextCompat.getColor(getContext(), R.color.custom_color5); // Get color from colors.xml
            tab.setSelectedTabIndicatorColor(tabTextColor);
            tab.setTabTextColors(black, tabTextColor);
        } else if(colour == 6){
            int tabTextColor = ContextCompat.getColor(getContext(), R.color.custom_color6); // Get color from colors.xml
            tab.setSelectedTabIndicatorColor(tabTextColor);
            tab.setTabTextColors(black, tabTextColor);
        } else{
            int tabTextColor = ContextCompat.getColor(getContext(), R.color.custom_color3); // Get color from colors.xml
            tab.setSelectedTabIndicatorColor(tabTextColor);
            tab.setTabTextColors(black, tabTextColor);
        }

        SharedPreferences pref_nnn = getActivity().getSharedPreferences("pref_Mudit", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref_nnn.edit();


        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
                editor.putInt("frag_pos", position);
                editor.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        return v;
    }


}