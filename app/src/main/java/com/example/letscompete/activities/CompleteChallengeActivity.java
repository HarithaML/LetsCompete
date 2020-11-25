package com.example.letscompete.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.letscompete.R;
import com.example.letscompete.adapters.AdapterParticipant;
import com.example.letscompete.adapters.AdapterVideo;
import com.example.letscompete.models.ModelParticipant;
import com.example.letscompete.models.ModelUser;
import com.example.letscompete.models.ModelVideo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class CompleteChallengeActivity extends AppCompatActivity {


    //action bar
    private ActionBar actionBar;
    private EditText titleEt;
    private VideoView videoView;
    private Button uploadVideoButton;
    private FloatingActionButton pickVideoFab;
    private TextView messageWindow;
    private Button challengeComplete;


    private static final int VIDEO_PICK_GALLERY_CODE = 100;
    private static final int VIDEO_PICK_CAMERA_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;

    private String[] cameraPermissions;

    private Uri videoUri;
    //uri of picked Video

    private String challengeTitle;
    private FirebaseAuth firebaseAuth;
    String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_challenge);

        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("challengeTitle")!= null)
        {
            //TODO here get the string stored in the string variable and do
            challengeTitle = bundle.getString("challengeTitle");
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            // user is signed in stay here
            //set email of logged in user
//            mProfileTv.setText(user.getEmail());
            userUID = user.getUid();

            //save uid of currently signed in user in shared preferences

        }else{
            System.out.println("User null");
        }

        //init views
        titleEt = findViewById(R.id.titleEt);
        videoView = findViewById(R.id.videoView);
        uploadVideoButton = findViewById(R.id.uploadVideoButton);
        pickVideoFab = findViewById(R.id.pickVideoFab);
        messageWindow = findViewById(R.id.messageWidow);
        challengeComplete = findViewById(R.id.confirmCompletion);

        //init action bar
        actionBar = getSupportActionBar();
        //title
        actionBar.setTitle("Complete Challenge");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //camera permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //challenge complete button
        challengeComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Participants");
                //get all data from path
                Query complete = ref.orderByChild("challengeTitle").equalTo(challengeTitle).orderByChild("userUID").equalTo(userUID);
                complete.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:  snapshot.getChildren()){
                                snapshot.getRef().child("status").setValue("Completed");
                                Toast.makeText(CompleteChallengeActivity.this,"status has been set",Toast.LENGTH_LONG);
                                startActivity(new Intent(CompleteChallengeActivity.this,TimeChallengeActivity.class));
                            }
                        }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }});

        //handle click upload video
        uploadVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = titleEt.getText().toString();
                if(TextUtils.isEmpty(description)){
                    Toast.makeText(CompleteChallengeActivity.this,"Title is required",Toast.LENGTH_SHORT).show();
                }else if(videoUri == null){
                    Toast.makeText(CompleteChallengeActivity.this,"Pick a video before you can upload",Toast.LENGTH_SHORT).show();
                }
                uploadVideoFirebase();
            }
        });

        //handle click,pick video from camera/gallery
        pickVideoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPickDialog();
            }
        });


    }



    private void uploadVideoFirebase() {
        ///timestamp
        final String timestamp = ""+System.currentTimeMillis();

        //file path and name in firebase storage
        String filePathAndName = "ChallengeVideos/" + "video_" + timestamp;

        //storage reference
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);

        //upload Video , you can upload any type of file using this method :)
        storageReference.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // video uploaded, get url of uploaded video
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();
                if(uriTask.isSuccessful()){
                    //url of uploaded video is recieved
                    //now we can video to firebase
                    //video uri
                    //challenge title
                    //user uid
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("videoUrl",""+downloadUri);
                    hashMap.put("challengeTitle",challengeTitle);
                    hashMap.put("uuid",userUID);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ChallengeVideos");
                    reference.push().setValue(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //video details added to db
                                    Toast.makeText(CompleteChallengeActivity.this,"Added video",Toast.LENGTH_SHORT).show();
                                    messageWindow.setText("Video Uploaded");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CompleteChallengeActivity.this,"Add video before uploading",Toast.LENGTH_SHORT).show();
                            messageWindow.setText("Add video before uploading");
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CompleteChallengeActivity.this,"video not uploaded",Toast.LENGTH_SHORT).show();
                messageWindow.setText("Video Not Uploaded");
            }
        });
    }

    private void videoPickDialog() {
        //options to diaplay in dialog
        String[] options ={"Camera", "Gallery"};

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Video From")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            //camera clicked
                            if(!checkCameraPermission()){
                                requestCameraPermission();
                            }else{
                                videoPickCamera();
                            }
                        }
                        if(which==1){
                            //gallery clicked
                            videoPickGallery();
                        }
                    }
                }).show();

    }

    private void requestCameraPermission(){
        //request camera permission
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==
                PackageManager.PERMISSION_GRANTED;
        boolean result2 = ContextCompat.checkSelfPermission(this,Manifest.permission.WAKE_LOCK)==
                PackageManager.PERMISSION_GRANTED;
        return result1 && result2;
    }

    private void videoPickGallery(){
        //pick video from gallery intent
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select Videos"),VIDEO_PICK_GALLERY_CODE);
    }
    private void videoPickCamera(){
        //pick video from camera - intent

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,VIDEO_PICK_CAMERA_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //handle permission results

        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        videoPickCamera();
                    }else{
                        Toast.makeText(this,"Camera & Storage permission are required",Toast.LENGTH_SHORT).show();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       //called after picking video from gallery/camera
        if(resultCode == RESULT_OK){
            if(requestCode == VIDEO_PICK_GALLERY_CODE){
                videoUri = data.getData();
                setVideoToView();
            }else if(requestCode == VIDEO_PICK_CAMERA_CODE){
                videoUri = data.getData();
                setVideoToView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setVideoToView() {
        MediaController mediaController = new MediaController(CompleteChallengeActivity.this);
        mediaController.setAnchorView(videoView);


        videoView.setMediaController(null);
        //set video uri
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}