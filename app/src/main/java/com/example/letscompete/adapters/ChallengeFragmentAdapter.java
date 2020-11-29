package com.example.letscompete.adapters;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.letscompete.fragments.ChallengeVideosFragment;
import com.example.letscompete.fragments.InfoFragment;
import com.example.letscompete.fragments.ParticipantsFragment;


public class ChallengeFragmentAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    String challenegTitle;
    String username;

    public ChallengeFragmentAdapter(Context context, FragmentManager fm, int totalTabs,String challenegTitle,String username) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.challenegTitle = challenegTitle;
        this.username = username;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return InfoFragment.newInstance(challenegTitle,username);
            case 1:
                return ParticipantsFragment.newInstance(challenegTitle);
            case 2:
                return ChallengeVideosFragment.newInstance(challenegTitle);
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
