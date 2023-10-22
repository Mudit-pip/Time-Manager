package com.timemanager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.timemanager.fragments.fragment_friday;
import com.timemanager.fragments.fragment_monday;
import com.timemanager.fragments.fragment_saturday;
import com.timemanager.fragments.fragment_sunday;
import com.timemanager.fragments.fragment_thursday;
import com.timemanager.fragments.fragment_tuesday;
import com.timemanager.fragments.fragment_wednesday;

public class VPadapter extends FragmentPagerAdapter {


    public VPadapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new fragment_monday();
        } else if (position == 1) {
            return new fragment_tuesday();
        } else if (position == 2) {
            return new fragment_wednesday();
        } else if (position == 3) {
            return new fragment_thursday();
        } else if (position == 4) {
            return new fragment_friday();
        } else if (position == 5) {
            return new fragment_saturday();
        } else {
            return new fragment_sunday();
        }
    }

    @Override
    public int getCount() {
        return 7;
    }




    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Monday";
        } else if (position == 1) {
            return "Tuesday";
        } else if (position == 2) {
            return "Wednesday";
        } else if (position == 3) {
            return "Thursday";
        } else if (position == 4) {
            return "Friday";
        } else if (position == 5) {
            return "Saturday";
        } else {
            return "Sunday";
        }
    }
}
