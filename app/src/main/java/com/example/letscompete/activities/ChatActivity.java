package com.example.letscompete.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.letscompete.adapters.AdapterChat;
import com.example.letscompete.models.ModelChat;
import com.example.letscompete.R;
import com.example.letscompete.models.ModelUser;
import com.example.letscompete.notifications.APIService;
import com.example.letscompete.notifications.Client;
import com.example.letscompete.notifications.Data;
import com.example.letscompete.notifications.Response;
import com.example.letscompete.notifications.Sender;
import com.example.letscompete.notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class ChatActivity extends AppCompatActivity {
    //views from xml
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileTv;
    TextView nameTv, userStatusTv;
    EditText messageEt;
    ImageButton sendBtn;

    FirebaseAuth firebaseAuth;
    APIService apiService;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    String hisUid;
    String myUid;
    String hisImage;

    DatabaseReference userRefForSeen;
    ValueEventListener seenListener;
    List<ModelChat> chatList;
    AdapterChat adapterChat;
    boolean notify = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //init views
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        recyclerView = findViewById(R.id.chat_recyclerView);
        profileTv = findViewById(R.id.profileTv);
        nameTv = findViewById(R.id.nameTv);
        userStatusTv = findViewById(R.id.userStatusTv);
        messageEt = findViewById(R.id.messageEt);
        sendBtn = findViewById(R.id.sendBtn);


        Intent intent = getIntent();
        hisUid = intent.getStringExtra("hisUID");

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        //Layout (LinearLayout) for RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //create api service
        apiService = Client.getRetrofit("https://fcm.googleapis.com/")
                .create(APIService.class);



        //search users to get that users info
        Query userQuery = databaseReference.orderByChild("uid").equalTo(hisUid);
        //get user picture and name
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check until required info is recieved
                for(DataSnapshot ds:  snapshot.getChildren()){
                    String name = ""+ds.child("name").getValue();
                    hisImage = ""+ds.child("image").getValue();
                    String typingStatus = ""+ds.child("typingTo").getValue();

                    if(typingStatus.equals(myUid)){
                        userStatusTv.setText("typing...");
                    }else {
                        //get value of onlinestatus
                        String onlineStatus = ""+ds.child("onlineStatus").getValue();
                        if (onlineStatus.equals("online")) {
                            userStatusTv.setText(onlineStatus);
                        } else {
                            //convert timestamp to dd/mm/yyyy hh:mm am/pm
                            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                            cal.setTimeInMillis(Long.parseLong(onlineStatus));
                            String dateTime = DateFormat.format("dd/MM/yyyy HH:mm:ss", cal)
                                    .toString();
                            String s = "Last seen at:" + dateTime;
                            userStatusTv.setText(s);

                        }
                    }
                    //set data
                    nameTv.setText(name);

                    try{
                        Picasso.get().load(hisImage)
                                .placeholder(R.drawable.ic_default_img_black)
                                .into(profileTv);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.ic_default_img_black)
                                .into(profileTv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //click button to send message
        sendBtn.setOnClickListener(v -> {
            System.out.println(" Send message button clicked");
            notify = true;
            // get text from edit text
            String message = messageEt.getText().toString().trim();
            //check if text is empty or not
            if(TextUtils.isEmpty(message)){
                Toast.makeText(ChatActivity.this,"Cannot Send Empty Message..",Toast.LENGTH_SHORT);

            }else{
                sendMessage(message);
            }
            messageEt.setText("");
        });
        //check edit text change listener
        messageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    checkTypingStatus("noOne");
                }else{
                    checkTypingStatus(hisUid);//uid of receiver
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        
        readMessage();
        seenMessage();
    }

    private void seenMessage() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(myUid)&&chat.getSender().equals(hisUid)){
                        HashMap<String, Object> hasSeenHasMap = new HashMap<>();
                        System.out.println("setting it true");
                        hasSeenHasMap.put("isSeen",true);
                        ds.getRef().updateChildren(hasSeenHasMap,new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Log.i("isSeen", "onComplete: success");
                                } else {
                                    Log.w("isSeen", "onComplete: fail", databaseError.toException());
                                }
                            }
                        });
                    }
                    adapterChat = new AdapterChat(ChatActivity.this,chatList,hisImage);
                    adapterChat.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage() {
        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(myUid)&& chat.getSender().equals(hisUid)||
                    chat.getReceiver().equals(hisUid)&&chat.getSender().equals(myUid)){
                        chatList.add(chat);
                    }
                    adapterChat = new AdapterChat(ChatActivity.this,chatList,hisImage);
                    adapterChat.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {

        /*
        Whenever the user sends the message
        it creates a new child in "Chats" node and that chhild will contain
        the following key values
        sender:uid
        reciever:uid
        message:actual message
         */
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",myUid);
        hashMap.put("receiver",hisUid);
        hashMap.put("message",message);
        hashMap.put("timestamp",timestamp);
        hashMap.put("isSeen",false);
        databaseReference.child("Chats").push().setValue(hashMap);

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelUser user = snapshot.getValue(ModelUser.class);
                if(notify){
                    sendNotification(hisUid,user.getName(),message);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(String hisUid, String name, String message) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(myUid, name+""+message, "New Message", hisUid, R.drawable.ic_default_img_black);
                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(ChatActivity.this,""+response.message(),Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // user is signed in stay here
            //set email of logged in user
//            mProfileTv.setText(user.getEmail());
            myUid = user.getUid();//currently signed in user's uid
        } else {
            // user not signed in go to main activity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    private void checkOnlineStatus(String status){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus",status);
        databaseReference.updateChildren(hashMap);

    }

    private void checkTypingStatus(String typing){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("typingTo",typing);
        databaseReference.updateChildren(hashMap);

    }
    @Override
    protected void onStart() {
        checkUserStatus();
        //set online
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //get timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());
        //set offline with last seen timestamp
        checkOnlineStatus(timestamp);
        checkTypingStatus("noOne");
        userRefForSeen.removeEventListener(seenListener);
    }

    @Override
    protected void onResume() {
        //set online
        checkOnlineStatus("online");
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        //hide search option
        menu.findItem(R.id.action_Search).setVisible(false);
        menu.findItem(R.id.action_search_challenge).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }
}