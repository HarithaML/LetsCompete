package com.example.letscompete.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.letscompete.AppDatabase;
import com.example.letscompete.DatabaseService;
import com.example.letscompete.R;
import com.example.letscompete.UserLeaderBoardStats;
import com.example.letscompete.adapters.CustomAdapter;
import com.example.letscompete.adapters.LeaderBoardAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderBoardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AppDatabase database;
    private DatabaseService service;
    private Intent sIntent;
    private boolean mBound = false;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder ibinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DatabaseService.DatabaseServiceBinder binder = (DatabaseService.DatabaseServiceBinder) ibinder;
            service = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LeaderBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeaderBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderBoardFragment newInstance(String param1, String param2) {
        LeaderBoardFragment fragment = new LeaderBoardFragment();
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
        database = AppDatabase.getInstance(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);
        sIntent = new Intent(view.getContext(), DatabaseService.class);
        UserLeaderBoardStats user = new UserLeaderBoardStats();
        user.setUsername("ok");
        user.setRank(1);
        user.setStat("12");
        database.userDao().insertAll(user);
        Button button = view.findViewById(R.id.button);
        //please change latter
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("touched", "yay");
                touch();
            }
        });
        // Inflate the layout for this fragment
        //setLeaderboardStats(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sIntent = new Intent(getView().getContext(), DatabaseService.class);
        getActivity().bindService(sIntent, connection, Context.BIND_AUTO_CREATE);
        getActivity().startService(sIntent);
        setLeaderboardStats(getView());
    }

    private void setLeaderboardStats(View view)
    {
        RecyclerView content = view.findViewById(R.id.leaderboard_list);
        List<UserLeaderBoardStats> ok = new ArrayList<>();
        ok.addAll(database.userDao().getAll());
        //Log.i("ok", ok.get(0).stat.toString());
        LeaderBoardAdapter ad = new LeaderBoardAdapter(ok);
        content.setAdapter(ad);
        content.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().stopService(sIntent);
        getActivity().unbindService(connection);
        database.clearAllTables();
    }

    private void touch()
    {
        if(mBound)
        {
            service.getDatabaseData();
            setLeaderboardStats(getView());
        }
    }

}