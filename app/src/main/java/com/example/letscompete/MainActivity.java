package com.example.letscompete;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.letscompete.Activities.LoginActivity;
import com.example.letscompete.Activities.RegisterActivity;

public class MainActivity extends AppCompatActivity {
    Button mRegisterBtn,mLoginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init views
        mRegisterBtn = findViewById(R.id.register_btn);
        mLoginBtn = findViewById(R.id.login_btn);

        mRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // start RegisterActivity
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start LoginActivity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }
}

/*Day-1
* 01-Add Internet permission (required for firebase)
* 02-Add Register and Login Buttons in Main Activity
* 03-Create Register Activity
* 04-Create FireBase Project and connect app with the project
* 05-Check google-services.json file to make sure app is connected with firebase
* 06-User Registration using Email& Password
* 07-Create ProfileActivity
* 08-Make ProfileActivity Launcher
* 09-Go to Profile Activity After Register/Login
* 10-Add Logout Feature
* */

/*Day-2
*01-Make ProfileActivity Launcher
*02-On app start check if user signed in stay in ProfileActivity otherwise go to MainActivity
* 03-Create Login Activity
* 04-Login User with Email/Password
* 05-After LoggingIn go to ProfileActivity
* 06-Add options menu for adding Logout Option
* 07-After LoggingOut go to MainActivity*/

/*Day-3
* 01-Add Google SignIn Feature
* Requirements:
* Enable Google SigIn from Firebase Authentication
* Add Project Support Email
* Add Sha-Certificate
* Add Google SignIN Library
*
*
* */

/*Day-4
01- Save Registered user's info(name,email,phone,image)
* in firebase realtime Database
Requirements:
1)Add Firebase RealTime database
2) Cahnge Firebase realtime Database rules
Add bottom navigation in Profile Activity having three menus
3) Home
4) Profile (User info like name,email,uid,phonr,image)
5)All  Users
* */

/*Day-5
*Design user profile
* Get User Info from firebase
* Show user info
* for setting image we will use picasso library
* */
