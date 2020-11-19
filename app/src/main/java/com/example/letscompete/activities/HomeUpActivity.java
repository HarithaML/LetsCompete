package com.example.letscompete.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import com.example.letscompete.R;
import com.example.letscompete.adapters.HomeUpAdapter;
import com.example.letscompete.fragments.ChallengeFragmentAdapter;
import com.example.letscompete.fragments.OngoingFragment;
import com.google.android.material.tabs.TabLayout;

public class HomeUpActivity  extends AppCompatActivity {
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        viewPager = findViewById(R.id.home_middle_content);

        //fragment transaction
        OngoingFragment ongoingFragment = new OngoingFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.home_middle_content, ongoingFragment,"");
        ft1.commit();

        TabLayout tabLayout = findViewById(R.id.home_up_tablayout);
        final HomeUpAdapter adapter = new HomeUpAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(HomeUpActivity.this, DashBoardActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
