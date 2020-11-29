package com.example.letscompete.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.letscompete.fragments.CompletedFragment;
import com.example.letscompete.fragments.OngoingFragment;

public class HomeUpAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    public HomeUpAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                System.out.println("loading ongoing challenge");
                return new OngoingFragment();
            case 1:
                System.out.println("loading completed challenges");
                return new CompletedFragment();
            default:
                return null;
        }
    }

        // this counts total number of tabs
        @Override
        public int getCount() {
            return totalTabs;
        }
}
