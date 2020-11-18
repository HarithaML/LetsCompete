package com.example.letscompete.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letscompete.Challengesfordisplay;
import com.example.letscompete.R;
import com.example.letscompete.activities.DashBoardActivity;
import com.example.letscompete.adapters.AdapterChallenges;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChallengeslistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChallengeslistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recview;
    AdapterChallenges adapter;

    public ChallengeslistFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ChallengeslistFragment newInstance(String param1, String param2) {
        ChallengeslistFragment fragment = new ChallengeslistFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_challengeslist, container, false);

        recview = (RecyclerView) view.findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Challengesfordisplay> options =
                new FirebaseRecyclerOptions.Builder<Challengesfordisplay>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Challengesfordsiplay"), Challengesfordisplay.class)
                        .build();


        adapter = new AdapterChallenges(options);
        recview.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void onBackPressed()
    {
        // code here to show dialog

        Intent intent = new Intent(getActivity(), DashBoardActivity.class);
        startActivity(intent);
        // optional depending on your needs
    }
}
