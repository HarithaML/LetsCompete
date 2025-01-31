package com.example.letscompete.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.letscompete.R;
import com.example.letscompete.activities.CreateChallengeActivity;
import com.example.letscompete.activities.JoinChallengeActivity;
import com.example.letscompete.activities.MainActivity;
import com.example.letscompete.adapters.HomeUpAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    ViewPager viewPager;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAuth firebaseAuth;

    //View for xml
    FloatingActionButton fab_add_challenge;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = view.findViewById(R.id.home_middle_content);
        OngoingFragment ongoingFragment = new OngoingFragment();
        FragmentTransaction ft1 = getChildFragmentManager().beginTransaction();
        ft1.add(R.id.home_middle_content, ongoingFragment,"");
        ft1.commit();
        TabLayout tabLayout = view.findViewById(R.id.home_up_tablayout);
        final HomeUpAdapter adapter = new HomeUpAdapter(getActivity().getApplicationContext(), getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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


        //init
        firebaseAuth = FirebaseAuth.getInstance();
        //init views
        fab_add_challenge = view.findViewById(R.id.fab_add_challenge);

        //floating add challenge
        fab_add_challenge.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showAddChallengeDialog();
            }
        });
        return view;
    }

    private void showAddChallengeDialog() {
        /* Add challenge options:
        * 1. create a new challenge
        * 2. join a current challenge
        * */
        String options[] = {"Create your challenge", "Join a challenge"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add new a challenge");
        builder.setItems(options, (dialog, which) -> {
            if(which == 0) {
                startActivity(new Intent(getActivity(), CreateChallengeActivity.class));
                getActivity().finish();
                //create a new challenge clicked
                //transfer to new challenge fragment_Harika
            } else if(which == 1) {
                //join a current challenge clicked
//                ChallengesListFragment fragment1 = new ChallengesListFragment();
//                FragmentTransaction ft1 = getFragmentManager().beginTransaction();
//                ft1.replace(R.id.content,fragment1,"");
//                ft1.addToBackStack(null).commit();
                startActivity(new Intent(getActivity(), JoinChallengeActivity.class));
                getActivity().finish();
            }
        });
        //create and show dialog
        builder.create().show();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflating menu
        inflater.inflate(R.menu.menu_main,menu);
        menu.findItem(R.id.action_Search).setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);
    }
    /* handle menu item clicks*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Toast
        //get item id
        int id= item.getItemId();
        if(id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }


    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            // user is signed in stay here
            //set email of logged in user
//            mProfileTv.setText(user.getEmail());
        }else{
            // user not signed in go to main activity
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }
}