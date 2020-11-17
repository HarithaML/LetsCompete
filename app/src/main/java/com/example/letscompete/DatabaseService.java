package com.example.letscompete;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.letscompete.models.ModelUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService extends Service {
    private final static String TAG = "MyService";
    FirebaseDatabase database;
    AppDatabase localDatabase;
    List<ModelUser> userList;

    public class DatabaseServiceBinder extends Binder {
        public DatabaseService getService(){
            return DatabaseService.this;
        }
    }

    private final IBinder myBinder = new DatabaseServiceBinder();

    public DatabaseService() {
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
        DatabaseReference a = database.getReference("Users");
        a.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                userList.clear();
                for(DataSnapshot ds:  dataSnapshot.getChildren()) {
                    ModelUser modelUser = ds.getValue(ModelUser.class);
                    userList.add(modelUser);
                }
                Log.i(TAG, "Value is: " + userList);
                for(ModelUser a: userList)
                {
                    UserLeaderBoardStats user = new UserLeaderBoardStats();
                    if(a.getName() != null){
                        user.setUsername(a.getName());
                    }
                    else
                    {
                        user.setUsername("none");
                    }
                    user.setStat("1");
                    localDatabase.userDao().insertAll(user);
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