package com.example.letscompete.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.letscompete.fragments.CompletedFragment;
import com.example.letscompete.fragments.OngoingFragment;

public class SectionPagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OngoingFragment();
            case 1:
                return new CompletedFragment();
            default:
                return new OngoingFragment();
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
