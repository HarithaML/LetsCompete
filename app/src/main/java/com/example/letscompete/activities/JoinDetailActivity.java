package com.example.letscompete.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.letscompete.R;
import com.example.letscompete.fragments.ChallengesListFragment;
import com.example.letscompete.models.ModelParticipant;
import com.example.letscompete.notifications.APIService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class JoinDetailActivity extends AppCompatActivity {
    ModelParticipant participants;
    String challengeTitle, challengeDuration, challengeDescription, challengeImageURL;
    String role = "Participant";
    String progress = "Inprogress";
    String status ="Ongoing";
    String rank = "0";
    DatabaseReference reference;

    FirebaseAuth firebaseAuth;
    APIService apiService;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Challenge Details");
        actionBar.setDisplayHomeAsUpEnabled(true);
        //get value from previous
        Intent intent = getIntent();
        challengeTitle = intent.getStringExtra("challengeTitle");
        challengeDuration = intent.getStringExtra("challengeDuration");
        challengeDescription = intent.getStringExtra("challengeDescription");
        challengeImageURL = intent.getStringExtra("challengeImageURL");
        //intia view
        Button button = findViewById(R.id.join_activity);
        Button Back = findViewById(R.id.back_activity);
        TextView name = findViewById(R.id.challengetitle_activity);
        TextView ChalDuration = findViewById(R.id.duration_activity);
        TextView ChalDescription = findViewById(R.id.description_activity);
        ImageView imageholder = findViewById(R.id.imagegholder_activity);
        name.setText(challengeTitle);
        ChalDuration.setText(challengeDuration);
        ChalDescription.setText(challengeDescription);
        Picasso.get().load(challengeImageURL).resize(362, 285).into(imageholder);
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

                Toast.makeText(JoinDetailActivity.this,"Joined Successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(JoinDetailActivity.this, DashBoardActivity.class));
                JoinDetailActivity.this.finish();
            }

        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(JoinDetailActivity.this, JoinChallengeActivity.class));
                JoinDetailActivity.this.finish();
            }

        });

    }//ends onCreate



    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(JoinDetailActivity.this, JoinChallengeActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}