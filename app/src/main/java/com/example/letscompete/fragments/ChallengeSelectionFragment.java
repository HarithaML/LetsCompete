package com.example.letscompete.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscompete.AppDatabase;
import com.example.letscompete.R;
import com.example.letscompete.UserLeaderBoardChallenges;
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
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String UPDATE_DATA = "jaskfnjksa";
    private AppDatabase database;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        //need androidx.fragment:fragment:1.3.0-alpha04 implementation?

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_challenge_selection, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        database = AppDatabase.getInstance(getActivity());
        View v = getView();
        RecyclerView content = v.findViewById(R.id.leaderboard_list2);
        List<UserLeaderBoardChallenges> ok = new ArrayList<>();
        /*
        UserLeaderBoardChallenges u = new UserLeaderBoardChallenges();
        u.setChallengename("Cooking Challenge");
        u.setPicture("https://firebasestorage.googleapis.com/v0/b/letscompete-209e6.appspot.com/o/Images%2F1605652627992.jpg?alt=media&token=d4224e99-a7ba-4077-ac38-bcdcf50b4f0d");
        u.setDuration("1/10/21");
        u.setType("Score");
        ok.add(u);
        */
        ok.addAll(database.leaderDao().getAll());
        Log.i("help", ok.size() + "");
        AdapterChallengesLeaderboard ad = new AdapterChallengesLeaderboard(ok, getActivity());
        content.setLayoutManager(new GridLayoutManager(v.getContext(), 3));
        content.setAdapter(ad);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}