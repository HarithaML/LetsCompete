package com.example.letscompete.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letscompete.R;
import com.example.letscompete.adapters.AdapterVideo;
import com.example.letscompete.models.ModelVideo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChallengeVideosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChallengeVideosFragment extends Fragment {

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

    //Videos
    private ArrayList<ModelVideo> videoArrayList;
    private AdapterVideo adapterVideo;
    private RecyclerView videosRv;

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

    public ChallengeVideosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param challengeTitle Parameter 1.
     * @return A new instance of fragment ChallengeVideosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChallengeVideosFragment newInstance(String challengeTitle) {
        ChallengeVideosFragment fragment = new ChallengeVideosFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_challenge_videos, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //videos
        videosRv = view.findViewById(R.id.videosRv2);
        loadVideosFromFireBase();
        return view;
    }
    private void loadVideosFromFireBase() {
        videoArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ChallengeVideos");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModelVideo modelVideo = ds.getValue(ModelVideo.class);
                    if(modelVideo.getChallengeTitle().equals(challengeTitle)){
                        videoArrayList.add(modelVideo);
                        adapterVideo = new AdapterVideo(getActivity(),videoArrayList);
                        videosRv.setAdapter(adapterVideo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        ModelVideo modelVideo = new ModelVideo();
//        modelVideo.setChallengeTitle("Test1");
//        modelVideo.setVideoUrl("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4");
//        videoArrayList.add(modelVideo);
//        ModelVideo modelVideo1 = new ModelVideo();
//        modelVideo1.setChallengeTitle("Test2");
//        modelVideo1.setVideoUrl("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4");
//        videoArrayList.add(modelVideo1);
//        adapterVideo = new AdapterVideo(getActivity(),videoArrayList);
//        videosRv.setAdapter(adapterVideo);
    }
}