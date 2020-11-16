package com.example.letscompete.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.letscompete.R;

public class MainActivity extends AppCompatActivity {
    Button mRegisterBtn,mLoginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /*
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                something();
            }
        });
*/
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
    public void something() {
        Intent intent = new Intent(this, Setting_Activity.class);
        startActivity(intent);
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
2) Change Firebase realtime Database rules
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

/*Day-6
->Edit Profile
1)Name
2)Phone
3)Profile Picture
4)Cover Photo
--> Requirements
1)Camera & Storage Permissions (to pick image from Camera or gallery)
2) FireBase Storage libraries (to upload a profile/cover photo)
--> Ui Update:
1)Image View for Cover Photo
2)Add Floating ActionButton to show dialog containing options to edit profile
3)Add default image for profile picture
 */

/*Day-7
->Search Users(Case Sensitive) User from User List
1) By Name
2) By Email

*First Add SearchView in menu_main.xml
*Move Options menu from DashBoard activity to each fragment
 */

/*Day-8
*-> Deign a Chat Activity [Create a new Empty Activity]
* Toolbar will contain receiver icon, name and status like online/offline.
* ->Add new Fragment For Chats list
* Add this fragment to BottomNavigation*/


/*Day-9
*-> Show reciever profile picture and name in toolbar
* ->send message to any user
* First Add Firebase Auth with logout in ChatActivity too
 */

/*Day-10
*->Show sent messages
* -Design different layouts for sender and receiver
* -I'll use custom background for sender and receiver you can download using link in description
* -Reciever layout(on left) will contain profile image, message and time
* -Sender Layout(on right) will contain message and time
* */

/*Day-11
<<<<<<< Updated upstream
->Last seen/online and seen/delivered
---- have to resolve bugs
 */

/*Day-12
-> show typing status of user in toolbar of chat_activity
When user Register and one more key named typing to having value receiver's uid
Add textChange listener to edit text
EditText not empty means user is typing something
so set it's value like receiver's uid
EditText empty means user is not typing
so set it's value like noOne
 */

/*
Day-13
-> Delete messages
Who can delete what?
1)Delete can delete only his own message or
2) Sender can delete his and receiver's message
After clicking delete what will happen?
1) Remove message OR
2) Update value of message to "This message was deleted....."
 */



