package com.example.letscompete.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letscompete.AppDatabase;
import com.example.letscompete.R;
import com.example.letscompete.entities.UserLeaderBoardChallenges;
import com.example.letscompete.adapters.AdapterChallengesLeaderboard;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChallengeSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChallengeSelectionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private final static String TAG="ChallengeSelectionFragment";

    private class RecieverData extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(UPDATE_DATA_SELECTION))
            {
                updateChallengesList();
                Log.i(TAG, "Got Message");
            }
        }
    }
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String UPDATE_DATA_SELECTION = "sdsdsdsd";
    private AppDatabase database;
    private RecyclerView recyclerView;
    private AdapterChallengesLeaderboard ad;
    List<UserLeaderBoardChallenges> challenges;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecieverData reciever;

    OnChallengeSelectionListener callback;

    public interface OnChallengeSelectionListener {
        public void onChallengeSelected(String name);
    }

    public void setOnChallengeSelectionListener(OnChallengeSelectionListener callback) {
        this.callback = callback;
    }


    public ChallengeSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChallengeSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChallengeSelectionFragment newInstance(String param1, String param2) {
        ChallengeSelectionFragment fragment = new ChallengeSelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        challenges = new ArrayList<>();
        database = AppDatabase.getInstance(getActivity());
        reciever = new RecieverData();
        //need androidx.fragment:fragment:1.3.0-alpha04 implementation?

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_challenge_selection, container, false);
        recyclerView = v.findViewById(R.id.leaderboard_list2);
        challenges.clear();
        challenges.addAll(database.leaderDao().getAll());
        //Log.i("help", challenges.size() + "");
        ad = new AdapterChallengesLeaderboard(challenges, getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(ad);
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        /*
        UserLeaderBoardChallenges u = new UserLeaderBoardChallenges();
        u.setChallengename("Cooking Challenge");
        u.setPicture("https://firebasestorage.googleapis.com/v0/b/letscompete-209e6.appspot.com/o/Images%2F1605652627992.jpg?alt=media&token=d4224e99-a7ba-4077-ac38-bcdcf50b4f0d");
        u.setDuration("1/10/21");
        u.setType("Score");
        ok.add(u);
        */
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(reciever,
                new IntentFilter(UPDATE_DATA_SELECTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(reciever);
    }


    private void updateChallengesList()
    {
        challenges.clear();
        challenges.addAll(database.leaderDao().getAll());
        ad.notifyDataSetChanged();
    }
}