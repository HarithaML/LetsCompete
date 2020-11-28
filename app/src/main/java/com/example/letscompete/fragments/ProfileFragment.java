package com.example.letscompete.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letscompete.activities.MainActivity;
import com.example.letscompete.R;
import com.example.letscompete.activities.Setting_Activity;
import com.example.letscompete.adapters.AdapterVideo;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //firebase
    FirebaseAuth  firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //storage
    StorageReference storageReference;
    //path where images of user profile and cover will be stored
    String storagePath = "Users_Profile_Cover_Imgs/";
    //permissions constants
    private static final int CAMERA_REQUEST_CODE =100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_REQUEST_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_REQUEST_CODE=400;

    //Uri of picked image
    Uri image_uri;

    //for checking profile or cover photo
    String profileOrCoverPhoto;


    //arrays of permissions to be requested
    String []cameraPermissions;
    String []storagePermissions;


    //views from xml
    ImageView avatarTv,coverTv;
    TextView name_tv, email_tv, phone_tv;
    FloatingActionButton fab;
    ImageButton setting;

    //Videos
    private ArrayList<ModelVideo> videoArrayList;
    private AdapterVideo adapterVideo;
    private RecyclerView videosRv;

    //other
    private String imgURL;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        //init arrays of Permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //init views
        avatarTv = view.findViewById(R.id.avatar_tv);
        name_tv = view.findViewById(R.id.name_tv);
        email_tv = view.findViewById(R.id.email_tv);
        phone_tv = view.findViewById(R.id.phone_tv);
        coverTv =view.findViewById(R.id.coverTv);
        fab =  view.findViewById(R.id.fab);
        setting = view.findViewById(R.id.ProfileSettingButton);
        /*We have to get info of currently signed in user. We can get it using user's email or uid
        I'm gonna retrieve user detail using email
        By using orderByChild query we will Show the detail from a node
        whose key named email has value equal to currently signed in email
       .
       It will search all nodes, where the key matches it will get its detail*/
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // checks until required data get
                for(DataSnapshot ds: snapshot.getChildren()){
                    String name = ""+ds.child("name").getValue();
                    String email = ""+ds.child("email").getValue();
                    String phone = ""+ds.child("phone").getValue();
                    imgURL = ""+ds.child("image").getValue();
                    String cover = ""+ds.child("cover").getValue();

                    //set data
                    name_tv.setText(name);
                    email_tv.setText(email);
                    phone_tv.setText(phone);
                    try{
                        //if image is recieved then set
                        Picasso.get().load(imgURL).into(avatarTv);
                    }catch(Exception e){
                        //if there is any exception in getting image
                        Picasso.get().load(R.drawable.ic_default_img_black).into(avatarTv);
                    }
                    try{
                        //if image is recieved then set
                        Picasso.get().load(cover).into(coverTv);
                    }catch(Exception e){
                        //if there is any exception in getting image
                        Picasso.get().load(R.drawable.ic_add_photo_black).into(coverTv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showEditProfileDialog();
                goToSettingsActivity();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showEditProfileDialog();
                goToSettingsActivity();
            }
        });


        //videos
        videosRv = view.findViewById(R.id.videosRv);
        loadVideosFromFireBase();

        // Inflate the layout for this fragment
        return view;
    }

    private void loadVideosFromFireBase() {
        videoArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ChallengeVideos");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    ModelVideo modelVideo = ds.getValue(ModelVideo.class);
                    if(modelVideo.getUuid().equals(user.getUid())){
                        videoArrayList.add(modelVideo);
                        adapterVideo = new AdapterVideo(getActivity(),videoArrayList);
                        videosRv.setAdapter(adapterVideo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        ModelVideo modelVideo = new ModelVideo();
//        modelVideo.setChallengeTitle("Test1");
//        modelVideo.setVideoUrl("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4");
//        videoArrayList.add(modelVideo);
//        ModelVideo modelVideo1 = new ModelVideo();
//        modelVideo1.setChallengeTitle("Test2");
//        modelVideo1.setVideoUrl("https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4");
//        videoArrayList.add(modelVideo1);
//        adapterVideo = new AdapterVideo(getActivity(),videoArrayList);
//        videosRv.setAdapter(adapterVideo);
    }

    private boolean checkStoragePermission(){
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
    }
    private void requestStoragePermission(){
        //request runtime Storage Permission
        requestPermissions(storagePermissions,STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result1 && result2;
    }
    private void requestCameraPermission(){
        //request runtime Storage Permission
       requestPermissions(cameraPermissions,CAMERA_REQUEST_CODE);
    }


    private void goToSettingsActivity()
    {
        Intent intent = new Intent(getActivity(), Setting_Activity.class);
        intent.putExtra("Image", imgURL);
        startActivity(intent);
    }

    private void showEditProfileDialog() {
        /*Show dialog containing options
        1)Edit Profile Picture
        2)Edit Cover Photo
        3)Edit Name
        4)Edit Phone
         */
        //options to show in dialog
        String options[] = {"Edit Profile Picture","Edit Cover Photo","Edit Name","Edit Phone number"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set Title
        builder.setTitle("Choose Action");
        //set items to dialog
        builder.setItems(options, (dialog, which) -> {
            //handle dialog items click
            if(which == 0){
                profileOrCoverPhoto="image";
                //Edit Profile Clicked
                showImagePicDialog();

            }else if(which == 1){
                profileOrCoverPhoto="cover";
                //Edit Cover Clicked
                showImagePicDialog();

            }else if(which == 2){
                //Edit Name Clicked
                //calling method and pass key "name" as parameter to update it's value in database
                showNamePhoneUpdateDialog("name");

            }else if(which == 3){
                //Edit Phone Clicked
                //calling method and pass key "phone" as parameter to update it's value in database
                showNamePhoneUpdateDialog("phone");


            }
        });
        //create and show dialog
        builder.create().show();
    }

    private void showImagePicDialog(){
        /*Show dialog containing options
        1)Camera
        2)Gallery

         */
        //options to show in dialog
        String options[] = {"Camera","Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    private void showNamePhoneUpdateDialog(String key){
        /*parameter "key" will contain value:
        either "name" which is key in user's database which is used to update user's name
        or "phone" which is key in user's database which is used to update user's phone
         */

        //custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update"+key);
        //set layout of dialog
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        //add edit text
        EditText editText = new EditText(getActivity());
        editText.setHint("Enter"+key);
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
                            Toast.makeText(getActivity(),"Updated"+key,Toast.LENGTH_SHORT);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT);
                        }
                    });

                }else{
                    Toast.makeText(getActivity(),"Please enter"+key,Toast.LENGTH_SHORT);
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
                       Toast.makeText(getActivity(),"Please enable camera & storage permissions",Toast.LENGTH_LONG);
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
                       Toast.makeText(getActivity(),"Please enable storage permissions",Toast.LENGTH_LONG);
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
                uploadProfileCoverPhoto(image_uri);

            }
            if(requestCode == IMAGE_PICK_CAMERA_REQUEST_CODE){
                uploadProfileCoverPhoto(image_uri);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

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
                                    Toast.makeText(getActivity(),"added to databse successfully",Toast.LENGTH_SHORT);
                                }
                            });


                }else{
                    Toast.makeText(getActivity(),"Error Occurred",Toast.LENGTH_SHORT);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Error Occurred",Toast.LENGTH_SHORT).show();
            }
        });

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
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_REQUEST_CODE);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflating menu
        inflater.inflate(R.menu.menu_main,menu);
        menu.findItem(R.id.action_Search).setVisible(false);
        menu.findItem(R.id.action_search_challenge).setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);
    }
    /* handle menu item clicks*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get item id
        int id= item.getItemId();
        if(id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }


    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            // user is signed in stay here
            //set email of logged in user
//            mProfileTv.setText(user.getEmail());
        }else{
            // user not signed in go to main activity
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }
}