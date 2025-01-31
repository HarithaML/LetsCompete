package com.example.letscompete.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.letscompete.R;
import com.example.letscompete.models.ModelParticipant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChallengeDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChallengeDescriptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ModelParticipant participants;
    String title, duration, description, imageurl;
    String role = "Participant";
    String progress = "Inprogress";
    String status ="Ongoing";
    String rank = "0";
    DatabaseReference reference;
    private AppCompatActivity activity;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";

    public ChallengeDescriptionFragment() {
        // Required empty public constructor
    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chaldescriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public ChallengeDescriptionFragment(String title, String duration, String description, String imageurl) {

        this.title = title;
        this.duration = duration;
        this.description = description;
        this.imageurl = imageurl;


    }

    public static ChallengeDescriptionFragment newInstance(String param1, String param2) {
        ChallengeDescriptionFragment fragment = new ChallengeDescriptionFragment();
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
        View view = inflater.inflate(R.layout.fragment_challenge_description, container, false);
        TextView name = view.findViewById(R.id.challengetitle);
        TextView ChalDuration = view.findViewById(R.id.duration);
        TextView ChalDescription = view.findViewById(R.id.description);
        ImageView imageholder = view.findViewById(R.id.imagegholder);
        Button button = (Button) view.findViewById(R.id.buttonjoin);
        Button Back = (Button) view.findViewById(R.id.button2);

        name.setText(title);
        ChalDuration.setText(duration);
        ChalDescription.setText(description);

        Picasso.get().load(imageurl).resize(362, 285).into(imageholder);
        //Picasso.get().load(imageUri).into(imageholder);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String userid = currentFirebaseUser.getUid();
                String username = currentFirebaseUser.getEmail();
                participants = new ModelParticipant();
                reference = FirebaseDatabase.getInstance().getReference().child("Participants");
                participants.setUserUID(userid);
                participants.setProgress(progress);
                participants.setRank(rank);
                participants.setRole(role);
                participants.setStatus(status);
                participants.setUserName(username);
                //participants.setUserImage(userimage.toString());
                //participants.setImageURL(imageurl1);
                participants.setChallengeTitle(name.getText().toString().trim());
                reference.push().setValue(participants);

                Toast.makeText(getActivity(),"Joined Successfully",Toast.LENGTH_SHORT).show();
                ChallengesListFragment fragment1 = new ChallengesListFragment();
                FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                ft1.replace(R.id.content,fragment1,"");
                ft1.addToBackStack(null).commit();
            }

        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ChallengesListFragment fragment1 = new ChallengesListFragment();
                FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                ft1.replace(R.id.content,fragment1,TAG_FRAGMENT);
                ft1.addToBackStack(null).commit();

            }

        });
        return view;

    }

}