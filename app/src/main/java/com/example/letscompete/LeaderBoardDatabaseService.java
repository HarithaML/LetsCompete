package com.example.letscompete;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.letscompete.fragments.LeaderBoardFragment;
import com.example.letscompete.models.ModelChallenge;
import com.example.letscompete.models.ModelParticipant;
import com.example.letscompete.models.ModelUser;
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
    private int count;
    private boolean isReady;

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
        isReady = false;
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
        getDatabaseData(name, false);
    }

    public void getDatabaseData(String name, boolean isSame)
    {
        isReady = false;
        Log.i(TAG, "Checking if this works");
        DatabaseReference a = database.getReference("Participants");
        Query query = a.orderByChild("challengeTitle").equalTo(name);
        if(!isSame)
        {
            localDatabase.userDao().deleteAll();
        }
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
                List<UserLeaderBoardStats> users = new ArrayList<>();
                for(int i = 0; i < userList.size(); i++)
                {
                    users.add(new UserLeaderBoardStats());
                    String rank = userList.get(i).getRank();
                    String username = userList.get(i).getUserName();
                    if(username != null){
                        users.get(i).setUsername(username);
                    }
                    else
                    {
                        users.get(i).setUsername("none");
                    }
                    if(rank!= null && !rank.isEmpty())
                    {
                        users.get(i).setStat(rank);
                    }
                    else
                    {
                        users.get(i).setStat("0");
                    }
                    //localDatabase.userDao().insertAll(user);
                    //localDatabase.userDao().insertAll(user);
                    //Log.i(TAG, "Value is: " + a);
                }
                localDatabase.userDao().insertAllList(users);
                getUserProfilePic(users);
                //stopSelf();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        //stopSelf();
    }
    private void getUserProfilePic(List<UserLeaderBoardStats> users) {
        count = 0;
        for (UserLeaderBoardStats user : users) {
            String name = user.getUsername();
            DatabaseReference b = database.getReference("Users");
            Query query2 = b.orderByChild("email").equalTo(name);
            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ++count;
                    List<ModelUser> modelusers = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ModelUser modeluser = ds.getValue(ModelUser.class);
                        modelusers.add(modeluser);
                    }
                    if (modelusers.size() == 0) {
                        if(count >= userList.size())
                        {
                            sendMessage();
                        }
                        return;
                    }
                    ModelUser c = modelusers.get(0);
                    Log.i(TAG, "Got data " + c.getImage());
                    if (c == null) {
                        return;
                    }
                    localDatabase.userDao().updatePicture(c.getImage(), c.getEmail());
                    if(count >= userList.size())
                    {
                        sendMessage();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    ++count;
                }

            });
        }
        /*
        while(count < users.size())
        {
            Log.i(TAG, count + "");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

         */
        isReady = true;
    }

    private void sendMessage()
    {
        Intent intent = new Intent(LeaderBoardFragment.UPDATE_DATA);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public boolean isDataReady()
    {
        return isReady;
    }
}