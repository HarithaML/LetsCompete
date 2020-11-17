package com.example.letscompete.fragments;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class ChallengeFragmentAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public ChallengeFragmentAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                InfoFragment infoFragment = new InfoFragment();
                return infoFragment;
            case 1:
                ParticipantsFragment participantsFragment = new ParticipantsFragment();
                return participantsFragment;
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
