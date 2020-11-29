package com.example.letscompete.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscompete.adapters.CustomAdapter;
import com.example.letscompete.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Setting_Activity extends AppCompatActivity {

    private static final String TAG= "Setting_Activity";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    DatabaseReference databaseReference;
    //Uri of picked image
    Uri image_uri;

    //path where images of user profile and cover will be stored
    final String storagePath = "Users_Profile_Cover_Imgs/";

    //for checking profile or cover photo
    String profileOrCoverPhoto;

    //storage
    StorageReference storageReference;

    //arrays of permissions to be requested
    String []cameraPermissions;
    String []storagePermissions;
    ImageView avatarTv;

    private static final int CAMERA_REQUEST_CODE =100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE=400;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_layout);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        //init arrays of Permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        actionBar = getSupportActionBar();
        actionBar.setTitle("Settings");
        actionBar.setDisplayHomeAsUpEnabled(true);

        RecyclerView content = findViewById(R.id.listSettings);
        String[] main = {getString(R.string.settings0),
                getString(R.string.settings1),
                getString(R.string.settings2),
                getString(R.string.settings3),
                getString(R.string.settings4)
        };
        String[] sub = {"",
                getString(R.string.settings1des),
                getString(R.string.settings2des),
                getString(R.string.settings3des),
                ""
        };
        avatarTv = (ImageView) findViewById(R.id.imageView);
        try{
            //if image is recieved then set
            Picasso.get().load(getIntent().getStringExtra("Image")).into(avatarTv);
        }catch(Exception e){
            //if there is any exception in getting image
            Picasso.get().load(R.drawable.ic_default_img_black).into(avatarTv);
        }
/*
        try{
            //if image is recieved then set
            Picasso.get().load(R.drawable.ic_default_img_black).into(avatarTv);
        }catch(Exception e){
            //if there is any exception in getting image
            Picasso.get().load(R.drawable.ic_default_img_black).into(avatarTv);
        }
        */
        CustomAdapter ad = new CustomAdapter(main,sub, this);
        content.setAdapter(ad);
        LinearLayoutManager a = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        a.setStackFromEnd(true);
        content.setLayoutManager(a);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    //fu
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

    //camera or gallery
    public void showImagePicDialog(){
        /*Show dialog containing options
        1)Camera
        2)Gallery

         */
        //options to show in dialog
        String[] options = {"Camera","Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    //pick from Gallery or camera
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

    //now uploading
    private void uploadProfileCoverPhoto(Uri uri) {
        //path and name of image to be stored in firebase storage
        String filePathAndName = storagePath+""+profileOrCoverPhoto+"_"+user.getUid();
        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //image is uploaded, now get it's uri and store it in database
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();
                //check if image is uploaded or not and uri is recieved
                if(uriTask.isSuccessful()){
                    //image uploaded
                    //add/update uri in user's database
                    HashMap<String,Object> results = new HashMap<>();
                    results.put(profileOrCoverPhoto,downloadUri.toString());
                    databaseReference.child(user.getUid()).updateChildren(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"added to databse successfully",Toast.LENGTH_SHORT).show();
                                }
                            });


                }else{
                    Toast.makeText(getApplicationContext(),"Error Occurred",Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error Occurred",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //After picking up from camera or gallery
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_REQUEST_CODE){
                //get uri of image
                image_uri = data.getData();
                uploadProfileCoverPhoto(image_uri);

            }
            if(requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE){
                uploadProfileCoverPhoto(image_uri);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showNamePhoneUpdateDialog(String key){
        /*parameter "key" will contain value:
        either "name" which is key in user's database which is used to update user's name
        or "phone" which is key in user's database which is used to update user's phone
         */

        //custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update "+key);
        //set layout of dialog
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //add edit text
        EditText editText = new EditText(this);
        editText.setHint("Enter "+key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        //add button in dialog to update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                String value= editText.getText().toString().trim();
                //validate if user has entered something or not
                if(!TextUtils.isEmpty(value)){
                    HashMap<String,Object> result = new HashMap<>();
                    result.put(key,value);
                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    finish();
                                    Toast.makeText(getApplicationContext(),"Updated "+key,Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(),"Please enter "+key,Toast.LENGTH_SHORT).show();
                }

            }
        });
        //add button in dialog to cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //create and show dialog
        builder.create().show();
    }

    public void changePassword()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Password");
        //set layout of dialog
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //add edit text
        EditText editText = new EditText(this);
        editText.setHint("Old Password");
        editText.setPadding(20,15,20,15);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        EditText editText2 = new EditText(this);
        editText2.setHint("New Password");
        editText2.setPadding(20,15,20,15);
        editText2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        linearLayout.addView(editText);
        linearLayout.addView(editText2);

        builder.setView(linearLayout);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                String value= editText.getText().toString().trim();
                String value2= editText2.getText().toString().trim();
                //validate if user has entered something or not
                Log.d(TAG, user.getEmail() + " " + value);
                if(!TextUtils.isEmpty(value) && !TextUtils.isEmpty(value2)){
                    AuthCredential c = EmailAuthProvider.getCredential(user.getEmail(),value);
                    user.reauthenticate(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.updatePassword(value2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");
                                        Toast.makeText(getApplicationContext(),"User password updated.",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "User password update failed.");
                                    Toast.makeText(getApplicationContext(),"Password update failed.",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "User password update failed.");
                            Toast.makeText(getApplicationContext(),"Password update failed.",Toast.LENGTH_SHORT).show();

                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(),"Please enter Passwords", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //add button in dialog to cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void promptLogout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        //set layout of dialog
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseAuth.signOut();
                checkUserStatus();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkUserStatus();
            }
        });
        builder.create().show();
    }

    public void setProfileOrCoverPhoto(String profileOrCoverPhoto) {
        this.profileOrCoverPhoto = profileOrCoverPhoto;
    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // user is signed in stay here
            //set email of logged in user
//            mProfileTv.setText(user.getEmail());
        } else {
            // user not signed in go to main activity
            //FragmentManager manange = getSupportFragmentManager();
            //startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }


}