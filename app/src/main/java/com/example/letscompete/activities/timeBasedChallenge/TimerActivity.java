package com.example.letscompete.activities.timeBasedChallenge;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.letscompete.R;
import com.example.letscompete.activities.DashBoardActivity;
import com.example.letscompete.activities.activityBasedChallenge.ActivityBasedChallengeActivity;
import com.example.letscompete.activities.activityBasedChallenge.CompleteChallengeActivity;
import com.example.letscompete.fragments.HomeFragment;
import com.example.letscompete.models.ModelImage;
import com.example.letscompete.models.ModelParticipant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class TimerActivity<storageReference> extends AppCompatActivity {
    private static final String TAG = "TimerActivity";
    private ActionBar actionBar;
    private String challengeTitle;
    private String participKey;
    ProgressBar progressBar;
    //Chronometer
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;
    private String completeTimeinString;
    private TextView messageWindow;
    private int completeTimeinMin, completeTimeinSecond, completeTimeTotal;
    Button startbtn, pausebtn, resetbtn, challengeComplete;
    private Handler timerHandler;
    private Runnable timerRunnable;
    //image
    private EditText titleEt;
    private ImageView imageView;
    private Button uploadImageButton;
    private Button pickImageButton;
    Uri image_uri;
    //permissions constants
    private static final int CAMERA_REQUEST_CODE =100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE=400;
    //arrays of permissions to be requested
    String []cameraPermissions;
    String []storagePermissions;
    ///firebase
    private DatabaseReference databaseReference ;
    private StorageReference reference ;
    private String username;
    private FirebaseAuth firebaseAuth;
    String userUID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_complete_challenge);
        firebaseAuth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("challengeTitle")!= null) {
            //TODO here get the string stored in the string variable and do
            challengeTitle = bundle.getString("challengeTitle");
            username = bundle.getString("username");
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            // user is signed in stay here
            //set email of logged in user
//            mProfileTv.setText(user.getEmail());
            userUID = user.getUid();
            //save uid of currently signed in user in shared preferences
        }
        //init action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle("Complete: " + challengeTitle);
        actionBar.setDisplayHomeAsUpEnabled(true);
        messageWindow = findViewById(R.id.messageWidow_timebased);
        challengeComplete = findViewById(R.id.confirmCompletion_timebased);
        //TIMER
        timerHandler = new Handler();
        timerRunnable = new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, "TICK");
                timerHandler.postDelayed(timerRunnable, 1000);
            }
        };
        chronometer = (Chronometer) findViewById(R.id.complete_chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        startbtn = (Button) findViewById(R.id.startChronometer);
        pausebtn = (Button) findViewById(R.id.pauseChronometer);
        resetbtn = (Button) findViewById(R.id.resetChronometer);
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                    startTimer();
                }
            }
        });
        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running) {
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                    stopTimer();
                }
                completeTimeinString = chronometer.getText().toString();
                getTimeinNumbers(completeTimeinString);
            }
        });
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
            }
        });
        //------------------>//images
        //titleEt = findViewById(R.id.titleEt_timebased);
        imageView = findViewById(R.id.imageView_timebased);
        uploadImageButton = findViewById(R.id.uploadImageButton_timebased);
        pickImageButton = findViewById(R.id.pickImageButton_timebased);
        progressBar = findViewById(R.id.progressBar2_timebased);
        progressBar.setVisibility(View.INVISIBLE);
        //init arrays of Permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("ChallengeImages");
        reference = FirebaseStorage.getInstance().getReference("ChallengeImages");
        //challenge complete button
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }

        });
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image_uri!=null){
                    uploadToFirebase(image_uri);

                }else{
                    messageWindow.setText("Please select image");
                }
            }
        });
        challengeComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image_uri!=null){
                    System.out.println("line 198");
                    changeToCompleted();
                }else{
                    messageWindow.setText("Please select image");
                }
            }

        });
    }

    private void changeToCompleted() {
        //get key
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Participants");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:  snapshot.getChildren()){
                    ModelParticipant modelParticipant = ds.getValue(ModelParticipant.class);
                    if (modelParticipant.getUserUID() != null) {
                        if (modelParticipant.getUserUID().equals(userUID) && modelParticipant.getChallengeTitle().equals(challengeTitle)){
                            participKey = ds.getKey();
                            //setvalue
                            ref.child(participKey).child("status").setValue("Completed");
                            ref.child(participKey).child("rank").setValue(String.valueOf(completeTimeTotal));
                            System.out.println("check db   "+participKey);
                            startActivity(new Intent(TimerActivity.this, DashBoardActivity.class));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getTimeinNumbers(String completeTimeinString) {
        String splitChronometer[] = completeTimeinString.split("Time: ");
        String splitTime[] = splitChronometer[1].split(":");
        completeTimeinMin = Integer.parseInt(splitTime[0]);
        completeTimeinSecond = Integer.parseInt(splitTime[1]);
        completeTimeTotal = completeTimeinMin *60+completeTimeinSecond;
    }


    public void startTimer() {
        Log.d(TAG, "Timer started");
        timerHandler.post(timerRunnable);
    }

    public void stopTimer() {
        Log.d(TAG, "Timer stopped");
        timerHandler.removeCallbacks(timerRunnable);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                showQuitCompleteDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showQuitCompleteDialog();
    }

    private void showQuitCompleteDialog() {
        String options[] = {"Yes, discard", "No, stay in this page"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to discard your complete challenge?");
        builder.setItems(options, ((dialog, which) -> {
            if(which == 0){
                startActivity(new Intent(TimerActivity.this, TimeBasedChallengeActivity.class).putExtra("challengeTitle", challengeTitle));
                finish();
            }else if(which == 1){
            }
        }));
        //create and show dialog
        builder.create().show();
    }
    private void showImageDialog() {
        /*Show dialog containing options
        1)Camera
        2)Gallery

         */
        //options to show in dialog
        String options[] = {"Camera","Gallery"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        //set Title
        builder.setTitle("Pick Image From");
        //set items to dialog
        builder.setItems(options, (dialog, which) -> {
            //handle dialog items click
            if(which == 0){
                //Camera Clicked
                if(!checkCameraPermission()){
                    requestCameraPermission();
                }else{
                    pickFromCamera();
                }


            }else if(which == 1){
                //Gallery Clicked
                if(!checkStoragePermission()){
                    requestStoragePermission();
                }else{
                    pickFromGallery();
                }


            }
        });
        //create and show dialog
        builder.create().show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //After picking up from camera or gallery
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE){
                //get uri of image
                image_uri = data.getData();
                uploadImage(image_uri);

            }
            if(requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE){
                uploadImage(image_uri);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void uploadImage(Uri image_uri) {
        imageView.setImageURI(image_uri);
        messageWindow.setText(" ");
    }
    private void pickFromGallery() {
        //pic from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_REQUEST_CODE);
    }
    private void pickFromCamera() {
        //intent of picking image from camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");

        //put image uri
        image_uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_REQUEST_CODE);
    }
    private boolean checkStoragePermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
    }
    private void requestStoragePermission(){
        //request runtime Storage Permission
        requestPermissions(storagePermissions,STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result1 && result2;
    }
    private void requestCameraPermission(){
        //request runtime Storage Permission
        requestPermissions(cameraPermissions,CAMERA_REQUEST_CODE);
    }
    private void uploadToFirebase(Uri uri) {
        StorageReference fileRef = reference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ModelImage modelImage = new ModelImage();
                        modelImage.setImageUrl(uri.toString());
                        modelImage.setUserUid(userUID);
                        modelImage.setChallengeTitle(challengeTitle);
                        modelImage.setUserName(username);
                        String modelId = databaseReference.push().getKey();
                        databaseReference.child(modelId).setValue(modelImage);
                        messageWindow.setText("Uploaded Image");
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(uri));
    }
}
