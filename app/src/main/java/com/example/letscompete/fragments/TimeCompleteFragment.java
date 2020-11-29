package com.example.letscompete.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.letscompete.R;
import com.example.letscompete.activities.TimerActivity;
import com.example.letscompete.activities.timeBasedChallenge.TimeBasedChallengeActivity;
import com.example.letscompete.models.ModelImage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeCompleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeCompleteFragment extends Fragment {

    private static final String TAG = "TimeCompleteFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private String challengeTitle;

    //Chronometer
    Button startbtn, pausebtn, resetbtn;
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    //image
    private ArrayList<ModelImage> imageArrayList;
    private RecyclerView imageRv;

    //permissions constants
    private static final int CAMERA_REQUEST_CODE =100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE=400;
    //storage
    StorageReference storageReference;
    //arrays of permissions to be requested
    String []cameraPermissions;
    String []storagePermissions;

    public TimeCompleteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param challengeTitle Parameter 1.
     * @return A new instance of fragment TimeCompleteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeCompleteFragment newInstance(String challengeTitle) {
        TimeCompleteFragment fragment = new TimeCompleteFragment();
        Bundle args = new Bundle();
        args.putString("challengeTitle", challengeTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            challengeTitle = getArguments().getString("challengeTitle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        //xml layout initial
        View view = inflater.inflate(R.layout.fragment_time_complete, container, false);
        //TIMER
        chronometer = view.findViewById(R.id.complete_chronometer);
        startbtn = view.findViewById(R.id.startChronometer);
        pausebtn = view.findViewById(R.id.pauseChronometer);
        resetbtn = view.findViewById(R.id.resetChronometer);
        //button listener
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!running){
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                }
            }
        });
        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running){
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }
            }
        });
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
            }
        });
        //firebase inital
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        return view;
    }

    public void startThread(View view){
        for(int i = 0; i < 10; i++){
            Log.d(TAG, "start timer");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void stopThread(View view){

    }

}