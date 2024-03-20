package com.example.asassignment;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;


public class Profile extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;

    Button changePicture, signOut, saveChanges, back;

    ImageView profilePic;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    FirebaseAuth fAuth;

    private Bundle savedInstanceState;
    //private StorageReference StorageRef;

    private Uri mImageUri;

    private static final int PICK_IMAGE_REQUEST = 1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic = findViewById(R.id.profilePic);
        changePicture = findViewById(R.id.changePicture);
        editTextEmail = findViewById(R.id.changeEmail);
        editTextPassword = findViewById(R.id.changePassword);
        saveChanges = findViewById(R.id.saveChanges);
        signOut = findViewById(R.id.signOut);
        back = findViewById(R.id.back);
        fAuth = FirebaseAuth.getInstance();

        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        StorageReference pfp = storageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
        File localPfp = null;
        try {
            localPfp = File.createTempFile("images", ".jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File finalLocalPfp = localPfp;
        pfp.getFile(localPfp).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profile.this, "Download successful", Toast.LENGTH_SHORT).show();
                profilePic.setImageURI(Uri.parse(finalLocalPfp.getAbsolutePath()));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this, "Download Failed" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageLauncher.launch("image/*");
            }
        });
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                uploadImage();

            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
                fAuth.signOut();
                finish();
                Toast.makeText(Profile.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, HomePage.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        mImageUri = data.getData();
        profilePic.setImageURI(mImageUri);
    }

    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {

                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        //Toast.makeText(Profile.this, "Test if",Toast.LENGTH_SHORT).show();
                        mImageUri = result;
                        profilePic.setImageURI(mImageUri);
                    }

                }
            });

    private void uploadImage () {
        //Toast.makeText(Profile.this, "test", Toast.LENGTH_SHORT).show();
        if (mImageUri != null) {
            StorageReference fileReference = storageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(Profile.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this, "Upload Failed" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_LONG).show();
        }
    }


    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }

    public void setSavedInstanceState(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
    }
}