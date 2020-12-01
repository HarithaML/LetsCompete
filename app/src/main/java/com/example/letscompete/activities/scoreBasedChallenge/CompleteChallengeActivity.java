package com.example.letscompete.activities.scoreBasedChallenge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letscompete.R;
import com.example.letscompete.activities.DashBoardActivity;
import com.example.letscompete.activities.activityBasedChallenge.ActivityBasedChallengeActivity;
import com.example.letscompete.fragments.HomeFragment;
import com.example.letscompete.models.ModelImage;
import com.example.letscompete.models.ModelParticipant;
import com.example.letscompete.models.ModelScoreChallenge;
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

public class CompleteChallengeActivity extends AppCompatActivity  {


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
    private Button uploadImageButton;
    private Button pickImageButton;
    Uri image_uri;
    ModelScoreChallenge modelScoreChallenge;
    //permissions constants
    private static final int CAMERA_REQUEST_CODE =100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE=400;
    //arrays of permissions to be requested
    String []cameraPermissions;
    String []storagePermissions;
    ///firebase
    private DatabaseReference databaseReference,databaseReference1 ;
    private StorageReference reference,reference1 ;
    ProgressBar progressBar;

    //alarm
    private TextView mTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_challenge);
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
        uploadImageButton = findViewById(R.id.uploadImageButton);
        pickImageButton = findViewById(R.id.pickImageButton);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
        //init arrays of Permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("ChallengeImages");
        reference = FirebaseStorage.getInstance().getReference("ChallengeImages");
        challengeComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image_uri!= null) {
                    if(!titleEt.getText().toString().equals("")){
                        updateDataToFirebase(titleEt);
                        uploadImageToFirebase(image_uri);
                        setParticipantStatusCompleted();

                    }else{
                        messageWindow.setText("Enter your score");
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
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image_uri!=null){
                    uploadImageToFirebase(image_uri);

                }else{
                    messageWindow.setText("Please select image");
                }
            }
        });


    }

    private void setParticipantStatusCompleted() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/Participants");
        //get all data from path
        Query complete = ref.orderByChild("challengeTitle").equalTo(challengeTitle);
        complete.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:  snapshot.getChildren()){
                    ModelParticipant modelParticipant = ds.getValue(ModelParticipant.class);
                    if(modelParticipant.getUserUID().equals(userUID)){
                        ds.getRef().child("status").setValue("Completed");
                        Toast.makeText(CompleteChallengeActivity.this, "status has been set", Toast.LENGTH_LONG);
                        startActivity(new Intent(CompleteChallengeActivity.this,
                                DashBoardActivity.class));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void updateDataToFirebase(EditText titleEt) {
        modelScoreChallenge = new ModelScoreChallenge();
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("ScoreChallenge");

        modelScoreChallenge.setUserName(username);
        modelScoreChallenge.setUserUid(userUID);
        modelScoreChallenge.setChallengeTitle(challengeTitle);
        try{
            modelScoreChallenge.setScore(titleEt.getText().toString());}
        catch (Exception e)
        {
            modelScoreChallenge.setScore("0");
        }
        databaseReference1.push().setValue(modelScoreChallenge);
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