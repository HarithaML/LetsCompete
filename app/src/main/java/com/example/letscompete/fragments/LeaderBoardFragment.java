package com.example.letscompete.fragments;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.letscompete.AppDatabase;
import com.example.letscompete.services.LeaderBoardDatabaseService;
import com.example.letscompete.R;
import com.example.letscompete.entities.UserLeaderBoardStats;
import com.example.letscompete.adapters.LeaderBoardAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "LeaderBoardFragment";
    public static final String UPDATE_DATA = "asdnjoasndosan";
    private TextView rank, username, number, challengeName, challengeType, challengeDuration;
    private ImageView challengePicture, profilePic;
    private AppDatabase database;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private LeaderBoardDatabaseService service;
    private Intent sIntent;
    private boolean mBound = false;
    private RecieverData r;

    private class RecieverData extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(UPDATE_DATA))
                {
                    setLeaderboardStats(getView());
                }
        }
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder ibinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LeaderBoardDatabaseService.DatabaseServiceBinder binder = (LeaderBoardDatabaseService.DatabaseServiceBinder) ibinder;
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
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        r = new RecieverData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);
        sIntent = new Intent(view.getContext(), LeaderBoardDatabaseService.class);
        sIntent.putExtra("Challenge", getArguments().getString("Challenge"));
        challengeName = view.findViewById(R.id.lChallenge);
        challengeType = view.findViewById(R.id.ctype);
        challengeDuration = view.findViewById(R.id.cduration);
        challengePicture= view.findViewById(R.id.lchallengePic);
        profilePic= view.findViewById(R.id.profile);
        try {
            challengeName.setText(getArguments().getString("Challenge"));
            challengeType.setText("Type: " + getArguments().getString("Type"));
            challengeDuration.setText("Duration: " + getArguments().getString("Duration"));
        }
        catch(Exception e)
        {
            challengeName.setText("");
        }
        try {
            String pic = getArguments().getString("Picture");
            Log.i(TAG, pic);
            if(pic != null && pic != null)
            {
                Picasso.get().load(pic).into(challengePicture);
            }
        }
        catch(Exception e)
        {
            //Picasso.get().load(R.drawable.ic_default_img_black).into(challengePicture);
        }
        rank = view.findViewById(R.id.RankLeader);
        username = view.findViewById(R.id.UserLeader);
        number = view.findViewById(R.id.numberLeader);
        Log.i("Help", getArguments().getString("Challenge"));
        //database.userDao().insertAll(user);
        ImageButton button = view.findViewById(R.id.button);
        Button button2 = view.findViewById(R.id.change_challenge_btn);
        //please change latter
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("touched", "yay");
                touch();
            }
        });
        // Inflate the layout for this fragment
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("touched", "yay");
                changeFrags();
            }
        });
        //setLeaderboardStats(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sIntent = new Intent(getView().getContext(), LeaderBoardDatabaseService.class);
        try {
            sIntent.putExtra("Challenge", getArguments().getString("Challenge"));
            sIntent.putExtra("Type", getArguments().getString("Type"));
        }
        catch (Exception e)
        {

        }
        getActivity().bindService(sIntent, connection, Context.BIND_AUTO_CREATE);
        getActivity().startService(sIntent);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r,
                new IntentFilter(UPDATE_DATA));
    }

    private void setLeaderboardStats(View view)
    {
        Log.i(TAG, user.getEmail());
        //remember this check
        RecyclerView content = view.findViewById(R.id.leaderboard_list);
        List<UserLeaderBoardStats> ok = new ArrayList<>();
        ok.addAll(database.userDao().getAll());
        //Log.i("ok", ok.get(0).stat.toString());
        List<UserLeaderBoardStats> ownStats = database.userDao().getUser(user.getEmail());
        if(ownStats.size() == 1)
        {
            username.setText(ownStats.get(0).getUsername());
            rank.setText("Your Rank: " + (ok.indexOf(ownStats.get(0)) + 1 ));
            number.setText(ownStats.get(0).getStat());
            try{
                Picasso.get().load(ownStats.get(0).getPicture()).into(profilePic);
            }
            catch (Exception e)
            {
            }
        }
        else
        {
            username.setText(user.getEmail());
            rank.setText("Your Rank: No data");
            number.setText("N/A");
        }
        LeaderBoardAdapter ad = new LeaderBoardAdapter(ok);
        content.setAdapter(ad);
        content.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public void onPause() {
        super.onPause();
        //database.userDao().deleteAll();
        getActivity().stopService(sIntent);
        getActivity().unbindService(connection);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
        //database.clearAllTables();
    }

    private void touch()
    {
        if(mBound)
        {
            service.getDatabaseData(getArguments().getString("Challenge"),true);
            setLeaderboardStats(getView());
        }
    }

    private void changeFrags()
    {
        FragmentTransaction fm = getFragmentManager().beginTransaction().setCustomAnimations
                (R.anim.slide_in_right,R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_right);
        ChallengeSelectionFragment fragment5 = new ChallengeSelectionFragment();
        fm.replace(R.id.content,fragment5,"");
        fm.commit();

    }

}