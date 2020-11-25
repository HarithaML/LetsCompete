package com.example.letscompete;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.letscompete.models.ModelParticipant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardDatabaseService extends Service {
    private final static String TAG = "LeaderBoardDatabaseService";
    FirebaseDatabase database;
    AppDatabase localDatabase;
    List<ModelParticipant> userList;

    public class DatabaseServiceBinder extends Binder {
        public LeaderBoardDatabaseService getService(){
            return LeaderBoardDatabaseService.this;
        }
    }

    private final IBinder myBinder = new DatabaseServiceBinder();

    public LeaderBoardDatabaseService() {
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
                if(intent!= null) {
                    getDatabaseData(intent.getStringExtra("Challenge"));
                }
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

    public void getDatabaseData(String name)
    {
        Log.i(TAG, "Checking if this works");
        DatabaseReference a = database.getReference("Participants");
        Query query = a.orderByChild("challengeTitle").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
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
                for(ModelParticipant a: userList)
                {
                    String rank = a.getRank();
                    UserLeaderBoardStats user = new UserLeaderBoardStats();
                    if(a.getUserName() != null){
                        user.setUsername(a.getUserName());
                    }
                    else
                    {
                        user.setUsername("none");
                    }
                    if(rank!= null && !rank.isEmpty())
                    {
                        user.setStat(a.getRank());
                    }
                    else
                    {
                        user.setStat("0");
                    }
                    localDatabase.userDao().insertAll(user);
                    //Log.i(TAG, "Value is: " + a);
                }
                stopSelf();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        //stopSelf();
    }
}