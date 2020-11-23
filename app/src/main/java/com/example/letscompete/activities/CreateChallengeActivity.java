package com.example.letscompete.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.letscompete.models.ModelChallenge;
import com.example.letscompete.models.ModelParticipant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.letscompete.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.util.Calendar;


public class CreateChallengeActivity<storageReference> extends AppCompatActivity {
    private static final String TAG = "Uploaded";
    EditText ChallengeTitle, ChallengeDuration, ChallengeDescription, StartDate,txtdata;
    String text;
    private  Uri FilePathUri;
    ImageView imageView,imgview;
    DatePickerDialog datepicker;
    int Image_Request_Code = 7;
    Spinner ChallengeType;

    Button Create,btnbrowse,btnupload;
    DatabaseReference reference,databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Button btnSelect, btnUpload;
    private StorageReference mStorage;


    private ProgressDialog mProgressDialog;
    private AlertDialog mAlertDialog;
    ProgressDialog progressDialog ;

    private static final int CAMERA_REQUEST_CODE = 111;
    private static final

    int GALLERY_INTENT = 2;

    View view;

    ModelChallenge modelChallenge;
    String role = "Moderator";
    String status = "Completed";
    String progress ="Started";
    String rank = "0";


    ModelParticipant participants;
    String imageurl1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createchallenge);
        mStorage = FirebaseStorage.getInstance().getReference();
        ChallengeTitle = (EditText) findViewById(R.id.challengetitle);
        ChallengeDuration = (EditText) findViewById(R.id.challengeduration);
        ChallengeDescription = (EditText) findViewById(R.id.challengedescription);
        txtdata = (EditText)findViewById(R.id.txtdata);
        StartDate = (EditText) findViewById(R.id.startdate);
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");
        // get the Firebase  storage reference
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");
        btnbrowse = (Button) findViewById(R.id.btnbrowse);
        btnupload = (Button) findViewById(R.id.btnUpload);
        txtdata = (EditText) findViewById(R.id.txtdata);
        imgview = (ImageView) findViewById(R.id.image_view);
        progressDialog = new ProgressDialog(CreateChallengeActivity.this);// context name as per your project name


        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                UploadImage();

            }
        });

        ChallengeType = findViewById(R.id.challengetype);
        StartDate.setInputType(InputType.TYPE_NULL);
        StartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                datepicker = new DatePickerDialog(CreateChallengeActivity.this,
                                                  new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                StartDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datepicker.show();
                StartDate.setText(StartDate.getText());
            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.challengetypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ChallengeType.setAdapter(adapter);
        ChallengeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(parent.getContext(), "choose a challenge type", Toast.LENGTH_SHORT).show();
            }
        });


        Create = (Button) findViewById(R.id.create);
        ChallengeType = (Spinner) findViewById(R.id.challengetype);
        modelChallenge = new ModelChallenge();
        reference = FirebaseDatabase.getInstance().getReference().child("Challenge");
        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                String userid = currentFirebaseUser.getUid();
                String username = currentFirebaseUser.getEmail();
                Uri userimage = currentFirebaseUser.getPhotoUrl();
                modelChallenge.setChallengeTitle(ChallengeTitle.getText().toString().trim());
                modelChallenge.setChallengeDuration(ChallengeDuration.getText().toString().trim());
                modelChallenge.setChallengeDescription(ChallengeDescription.getText().toString().trim());
                modelChallenge.setStartdate(StartDate.getText().toString());
                modelChallenge.setChallengeType(text);
                modelChallenge.setImageName(txtdata.getText().toString().trim());
                modelChallenge.setImageURL(imageurl1);
                //challenge.setRole("Moderator");
                modelChallenge.setUserID(userid);
                reference.push().setValue(modelChallenge);

                participants = new ModelParticipant();
                reference = FirebaseDatabase.getInstance().getReference().child("Participants");
                participants.setUserUID(userid);
                participants.setProgress(progress);
                participants.setRank(rank);
                participants.setRole(role);
                participants.setStatus(status);
                participants.setUserName(username);
                //Qiming commented this following setter s
                //participants.setUserImage(userimage.toString());
                //participants.setImageURL(imageurl1);
                participants.setChallengeTitle(ChallengeTitle.getText().toString().trim());
                reference.push().setValue(participants);


                Toast.makeText(CreateChallengeActivity.this, "Challenge created successfully", Toast.LENGTH_SHORT).show();
                ChallengeTitle.setText("");
                ChallengeDuration.setText("");
                ChallengeDescription.setText("");
                StartDate.setText("");
                txtdata.setText("");
            }

        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    public void UploadImage() {

        if (FilePathUri != null) {

            progressDialog.setTitle("Image is Uploading...");
            progressDialog.show();
            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String TempImageName = txtdata.getText().toString().trim();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            @SuppressWarnings("VisibleForTests")

                            //uploadinfo imageUploadInfo = new uploadinfo(TempImageName, taskSnapshot.getUploadSessionUri().toString());
                                    ModelChallenge modelChallenge = new ModelChallenge(TempImageName, taskSnapshot.getUploadSessionUri().toString());
                            String ImageUploadId = databaseReference.push().getKey();
                            imageurl1 = taskSnapshot.getUploadSessionUri().toString();
                            databaseReference.child(ImageUploadId).setValue(modelChallenge);
                        }
                    });
        }
        else {

            Toast.makeText(CreateChallengeActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }

    }
}

