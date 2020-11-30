package com.example.letscompete;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.letscompete.fragments.ChallengeSelectionFragment;
import com.example.letscompete.models.ModelChallenge;
import com.example.letscompete.models.ModelParticipant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserLeaderBoardDatabaseService extends Service {
    private final static String TAG = "UserLeaderBoardDatabaseService";
    FirebaseDatabase database;
    FirebaseUser user;
    AppDatabase localDatabase;
    List<ModelParticipant> userList;
    Query query;

    public class DatabaseServiceBinder extends Binder {
        public UserLeaderBoardDatabaseService getService(){
            return UserLeaderBoardDatabaseService.this;
        }
    }

    private final IBinder myBinder = new DatabaseServiceBinder();

    public UserLeaderBoardDatabaseService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        database = FirebaseDatabase.getInstance();
        localDatabase = AppDatabase.getInstance(this);
        userList = new ArrayList<>();
        Log.i(TAG, "Starting Service");
        Log.i(TAG, "Thread ID: " + Thread.currentThread().getId());
        Runnable a= new Runnable(){
            @Override
            public void run() {
                Log.i(TAG, "Inside run()");
                getDatabaseData();
            }
        };
        Thread t = new Thread(a);
        t.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "On Destroy");
        super.onDestroy();
    }

    public void getDatabaseData()
    {
        Log.i(TAG, "Checking if this works");
        localDatabase.leaderDao().deleteAll();
        DatabaseReference a = database.getReference("Participants");
        user = FirebaseAuth.getInstance().getCurrentUser();
        query = a.orderByChild("userUID").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                userList.clear();
                for(DataSnapshot ds:  dataSnapshot.getChildren()) {
                    ModelParticipant modelUser = ds.getValue(ModelParticipant.class);
                    userList.add(modelUser);
                }
                Log.i(TAG, "Value is: " + userList + "\n" + userList.size());
                for(ModelParticipant b: userList)
                {
                    Log.i(TAG, b.toString());
                    DatabaseReference a = database.getReference("Challenge");
                    String challengeTitle = b.getChallengeTitle();
                    Query q = a.orderByChild("challengeTitle").equalTo(challengeTitle);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<ModelChallenge> modelchallenges = new ArrayList<>();
                            for(DataSnapshot ds:  snapshot.getChildren()) {
                                ModelChallenge modelchallenge = ds.getValue(ModelChallenge.class);
                                modelchallenges.add(modelchallenge);
                            }
                            if(modelchallenges.size() ==0)
                            {
                                return;
                            }
                            ModelChallenge c = modelchallenges.get(0);
                            Log.i(TAG, "Got data " + c);
                            if(c ==null) {
                                return;
                            }
                            UserLeaderBoardChallenges user = new UserLeaderBoardChallenges();
                            String challengeTitle = c.getChallengeTitle();
                            String challengeImage = c.getImageURL();
                            String challengeType = c.getChallengeType();
                            String challengeDuration = c.getChallengeDuration();
                            if(challengeImage != null){
                                user.setPicture(challengeImage);
                                Log.i(TAG, "Got data " + challengeImage);
                            }
                            if(challengeType != null){
                                user.setType(challengeType);
                                Log.i(TAG, "Got data " + challengeType);
                            }
                            if(challengeDuration != null){
                                user.setDuration(challengeDuration);
                                Log.i(TAG, "Got data " + challengeDuration);
                            }
                            if(challengeTitle != null){
                                user.setChallengename(challengeTitle);
                                Log.i(TAG, "Got data " + challengeTitle);
                                localDatabase.leaderDao().insertAll(user);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    /*
                    UserLeaderBoardChallenges user = new UserLeaderBoardChallenges();
                    if(challengeTitle != null){
                        user.setChallengename(challengeTitle);
                        Log.i(TAG, "Got data " + challengeTitle);
                    }
                    localDatabase.leaderDao().insertAll(user);
                     */
                    //Log.i(TAG, "Value is: " + a);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        //stopSelf();
    }

    //For Later
    private void sendMessage()
    {
        Intent intent = new Intent(ChallengeSelectionFragment.UPDATE_DATA);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}