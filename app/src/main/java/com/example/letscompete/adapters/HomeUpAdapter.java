package com.example.letscompete.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.letscompete.fragments.CompletedFragement;
import com.example.letscompete.fragments.InfoFragment;
import com.example.letscompete.fragments.OngoingFragment;
import com.example.letscompete.fragments.ParticipantsFragment;

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
                System.out.println("you are ongoing");
                OngoingFragment ongoingFragment = new OngoingFragment();
                return ongoingFragment;
            case 1:
                CompletedFragement completedFragement = new CompletedFragement();
                return completedFragement;
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
