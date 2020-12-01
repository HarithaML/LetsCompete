package com.example.letscompete.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.letscompete.services.UserLeaderBoardDatabaseService;
import com.example.letscompete.fragments.ChallengeSelectionFragment;
import com.example.letscompete.fragments.ChallengesListFragment;
import com.example.letscompete.fragments.ContactsFragment;
import com.example.letscompete.fragments.HomeFragment;
import com.example.letscompete.fragments.LeaderBoardFragment;
import com.example.letscompete.fragments.ProfileFragment;
import com.example.letscompete.R;
import com.example.letscompete.notifications.Token;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class DashBoardActivity extends AppCompatActivity
    implements ChallengeSelectionFragment.OnChallengeSelectionListener {
    // firebase auth
    private static String TAG = "DashBoardActivity";
    private UserLeaderBoardDatabaseService service;
    private Intent sIntent;
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;
    String mUID;
    private boolean mBound;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder ibinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            UserLeaderBoardDatabaseService.DatabaseServiceBinder binder = (UserLeaderBoardDatabaseService.DatabaseServiceBinder) ibinder;
            service = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //ActionBar and its title
        actionBar = getSupportActionBar();
        actionBar.setTitle("Home");
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);

        //init
        firebaseAuth = FirebaseAuth.getInstance();

        //bottom Navigation
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        //home fragment transaction
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,fragment1,"");
        ft1.commit();

        checkUserStatus();

        //update token
        updateToken(FirebaseInstanceId.getInstance().getToken());
        sIntent = new Intent(this, UserLeaderBoardDatabaseService.class);
        bindService(sIntent, connection, Context.BIND_AUTO_CREATE);
        startService(sIntent);

    }

    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(sIntent);
        unbindService(connection);
        Log.i(TAG, "onDestroy Dashboard");
    }

    public void updateToken(String token){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mtoken = new Token(token);
        ref.child(mUID).setValue(mtoken);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //handle item clicks
                    switch(item.getItemId()){
                        case R.id.nav_home:
                            //home fragment transaction
                            actionBar.setTitle("Home");
                            HomeFragment fragment1 = new HomeFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content,fragment1,"");
                            ft1.commit();
                            return true;
                        case R.id.nav_users:

                            actionBar.setTitle("Users");
                            ContactsFragment fragment2 = new ContactsFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content,fragment2,"");
                            ft2.commit();
                            return true;
                        case R.id.nav_Profile:
                            actionBar.setTitle("Profile");
                            ProfileFragment fragment3 = new ProfileFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content,fragment3,"");
                            ft3.commit();
                            return true;
                        case R.id.nav_leaderBoard:
                            actionBar.setTitle("LeaderBoard");
                            //LeaderBoardFragment fragment5 = new LeaderBoardFragment();
                            ChallengeSelectionFragment fragment5 = new ChallengeSelectionFragment();
                            Bundle args = new Bundle();
                            args.putString("Challenge", "Other");
                            fragment5.setArguments(args);
                            FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                            ft5.replace(R.id.content,fragment5,"");
                            ft5.commit();
                            return true;
                    }
                    return false;
                }
            };
    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            // user is signed in stay here
            //set email of logged in user
//            mProfileTv.setText(user.getEmail());
            mUID = user.getUid();

            //save uid of currently signed in user in shared preferences
            SharedPreferences sp = getSharedPreferences("SP_USER",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Current_USERID",mUID);
            editor.apply();
        }else{
            // user not signed in go to main activity
            startActivity(new Intent(DashBoardActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        // check on start of app
        checkUserStatus();
        super.onStart();
    }
    /*inflate options menu*/

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof ChallengeSelectionFragment) {
            ChallengeSelectionFragment headlinesFragment = (ChallengeSelectionFragment) fragment;
            headlinesFragment.setOnChallengeSelectionListener(this);
        }
    }

    public void onChallengeSelected(String name)
    {
        onChallengeSelected(name, "none", "none", null);
    }

    public void onChallengeSelected(String name, String type, String duration, String picture)
    {
        actionBar.setTitle("LeaderBoard");
        LeaderBoardFragment fragment5 = new LeaderBoardFragment();
        //ChallengeSelectionFragment fragment5 = new ChallengeSelectionFragment();
        Bundle args = new Bundle();
        args.putString("Challenge", name);
        args.putString("Type", type);
        args.putString("Duration", duration);
        args.putString("Picture", picture);
        fragment5.setArguments(args);
        FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction().setCustomAnimations(
                R.anim.fade_in,R.anim.slide_out_right, R.anim.fade_in, R.anim.slide_in_right);
        ft5.replace(R.id.content,fragment5,"");
        ft5.commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                HomeFragment fragment1 = new HomeFragment();
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.content,fragment1,"");
                ft1.addToBackStack(null).commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        BottomNavigationView mBottomNavigationView = findViewById(R.id.navigation);
        if (mBottomNavigationView.getSelectedItemId() == R.id.nav_home)
        {
            moveTaskToBack(true);
        }
        else
        {
            mBottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }
}