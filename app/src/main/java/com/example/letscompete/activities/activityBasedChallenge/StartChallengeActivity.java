package com.example.letscompete.activities.activityBasedChallenge;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.letscompete.R;
import com.example.letscompete.models.ModelActivityChallenge;
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

public class StartChallengeActivity extends AppCompatActivity{



    //action bar
    private ActionBar actionBar;

    private TextView messageWindow;
    private Button challengeComplete;


    private String challengeTitle;
    private String username;
    private FirebaseAuth firebaseAuth;
    String userUID;

    //image
    private EditText titleEt;
    private ImageView imageView;
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
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference ;
    private StorageReference reference ;
    ProgressBar progressBar;

    //steps
    ProgressBar progress ;
    private static final int REQUEST_WRITE_STORAGE = 112;
    Button start;
    Button stop;
    TextView counter;
//    TextView detector;
    String countedStep;
    String DetectedStep;
    static final String State_Count = "Counter";
    static final String State_Detect = "Detector";

    boolean isServiceStopped;

    private Intent intent;
    private static final String TAG = "SensorEvent";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_challenge);
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
        //init views
        messageWindow = findViewById(R.id.messageWidow);
        challengeComplete = findViewById(R.id.confirmCompletion);
        changeButtonVisibility();
        //init action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle("Complete: " + challengeTitle);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //------------------>//images
        titleEt = findViewById(R.id.titleEt);
        imageView = findViewById(R.id.imageView);
        pickImageButton = findViewById(R.id.pickImageButton);
        //init arrays of Permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("ChallengeImages");
        reference = FirebaseStorage.getInstance().getReference("ChallengeImages");
        //challenge complete button
        challengeComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image_uri!= null) {
                    if(!counter.getText().toString().equals("")){
                        updateDataToFirebase(counter);
                        uploadImageToFirebase(image_uri);
                        setParticipantStatusCompleted();

                    }else{
                        messageWindow.setText("Finish the activity");
                    }

                }else{
                    messageWindow.setText("Pick an image");
                }


            }});
        pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }

        });



        //steps
        progress = findViewById(R.id.progress_count);
        // ___ instantiate intent ___ \\
        //  Instantiate the intent declared globally - which will be passed to startService and stopService.
        intent = new Intent(this, StepCountingService.class);

        init(); // Call view initialisation method.



    }



    private void setParticipantStatusCompleted() {
        DatabaseReference ref = firebaseDatabase.getReference("Participants");
        //get all data from path
        Query complete = ref.orderByChild("challengeTitle").equalTo(challengeTitle);
        complete.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:  snapshot.getChildren()){
                    ModelParticipant modelParticipant = ds.getValue(ModelParticipant.class);
                    if(modelParticipant.getUserUID().equals(userUID)){
                        ds.getRef().child("status").setValue("Completed");
                        Toast.makeText(StartChallengeActivity.this, "status has been set", Toast.LENGTH_LONG);
                        startActivity(new Intent(StartChallengeActivity.this,
                                                 ActivityBasedChallengeActivity.class));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void changeButtonVisibility() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/Participants");
        Query complete = ref.orderByChild("userUID").equalTo(userUID);
        complete.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:  snapshot.getChildren()) {
                    ModelParticipant modelParticipant = ds.getValue(ModelParticipant.class);
                    if(modelParticipant.getStatus().equals("Completed") && modelParticipant.getChallengeTitle().equals(challengeTitle)){
                        challengeComplete.setVisibility(View.GONE);
                    }else{
                        challengeComplete.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    //image
    private void showImageDialog() {
        /*Show dialog containing options
        1)Camera
        2)Gallery

         */
        //options to show in dialog
        String options[] = {"Camera","Gallery"};

        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writtenStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writtenStorageAccepted){
                        //PERMISSIONS ENABLED
                        pickFromCamera();
                    }else{
                        // permissions denied
                        Toast.makeText(this,"Please enable camera & storage permissions",Toast.LENGTH_LONG);
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean writtenStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(writtenStorageAccepted){
                        //PERMISSIONS ENABLED
                        pickFromGallery();
                    }else{
                        // permissions denied
                        Toast.makeText(this,"Please enable storage permissions",Toast.LENGTH_LONG);
                    }
                }

            }
            break;
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //reload my activity with permission granted or use the features what required the permission
                    Toast.makeText(StartChallengeActivity.this, "The app was allowed to access storage", Toast.LENGTH_LONG).show();
                } else
                {
                    Toast.makeText(StartChallengeActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }
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
        messageWindow.setText("Image Picked");
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
    private void uploadImageToFirebase(Uri uri) {
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

    //steps
    /*string array to capture
     * a. start date/time,
     * b. step count
     * c. Distance walked
     * d. Total time taken*/

    // Initialise step_detector layout view

    // Initialise views.
    public void init() {
        isServiceStopped = true; // variable for managing service state - required to invoke "stopService" only once to avoid Exception.
        // ________________ Service Management (Start & Stop Service). ________________ //
        // ___ start Service & register broadcast receiver ___ \\
        start = (Button)findViewById(R.id.btn_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start Service.
                startService(new Intent(getBaseContext(), StepCountingService.class));
                // register our BroadcastReceiver by passing in an IntentFilter. * identifying the message that is broadcasted by using static string "BROADCAST_ACTION".
                registerReceiver(broadcastReceiver, new IntentFilter(StepCountingService.BROADCAST_ACTION));
                isServiceStopped = false;
            }
        });

        // ___ unregister receiver & stop service ___ \\
        stop = (Button)findViewById(R.id.btn_stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isServiceStopped) {
                    // call unregisterReceiver - to stop listening for broadcasts.
                    unregisterReceiver(broadcastReceiver);

                    // stop Service.
                    stopService(new Intent(getBaseContext(), StepCountingService.class));

                    isServiceStopped = true;

                }
            }
        });
        // ___________________________________________________________________________ \\
        counter = findViewById(R.id.counter);
//        detector = findViewById(R.id.detector);
    }

    private void updateDataToFirebase(TextView counter) {
        DatabaseReference ref = firebaseDatabase.getReference("ActivityChallenge");
        ModelActivityChallenge modelActivityChallenge  = new ModelActivityChallenge();
        modelActivityChallenge.setChallengeTitle(challengeTitle);
        modelActivityChallenge.setCounter(counter.getText().toString());
        modelActivityChallenge.setUserId(userUID);
        modelActivityChallenge.setUserName(username);
        String modelId = ref.push().getKey();
        ref.child(modelId).setValue(modelActivityChallenge);
    }

    protected void onPause() {
        super.onPause();
    }


    protected void onResume() {
        super.onResume();

    }
    // --------------------------------------------------------------------------- \\
    // ___ create Broadcast Receiver ___ \\
    // create a BroadcastReceiver - to receive the message that is going to be broadcast from the Service
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // call updateUI passing in our intent which is holding the data to display.
            updateViews(intent);
        }
    };
    // ___________________________________________________________________________ \\

    // ___ retrieve data from intent & set data to textviews __ \\
    private void updateViews(Intent intent) {
        // retrieve data out of the intent.
        countedStep = intent.getStringExtra("Counted_Step");
        DetectedStep = intent.getStringExtra("Detected_Step");
        Log.d(TAG, String.valueOf(countedStep));
        Log.d(TAG, String.valueOf(DetectedStep));
        progress.setProgress(Integer.parseInt(countedStep));
        counter.setText(String.valueOf(countedStep) );
//        detector.setText("Steps Detected = " + String.valueOf(DetectedStep));

    }
    // ___________________________________________________________________________ \\


}