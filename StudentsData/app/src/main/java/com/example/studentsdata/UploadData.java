package com.example.studentsdata;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentsdata.databinding.ActivityUploadDataBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class UploadData extends AppCompatActivity {

    private ActivityUploadDataBinding binding;

    Uri filePath;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadDataBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("StudentsData");



        binding.filelogo.setVisibility(View.INVISIBLE);
        binding.cancelfile.setVisibility(View.INVISIBLE);

        binding.cancelfile.setOnClickListener( v ->{
            binding.filelogo.setVisibility(View.INVISIBLE);
            binding.cancelfile.setVisibility(View.INVISIBLE);
            binding.imagebrowse.setVisibility(View.VISIBLE);

        });


        binding.imagebrowse.setOnClickListener(v ->{

            Dexter.withContext(getApplicationContext())
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                            Intent intent = new Intent();
                            intent.setType("application/pdf");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent,"Select Pdf file"),101);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();

                        }
                    }).check();

        });

        binding.imageupload.setOnClickListener(v ->{
            studentDataUpload(filePath);
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK){
            filePath = data.getData();
            binding.filelogo.setVisibility(View.VISIBLE);
            binding.cancelfile.setVisibility(View.VISIBLE);
            binding.imagebrowse.setVisibility(View.INVISIBLE);

        }
    }

    public void studentDataUpload(Uri filePath){


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File Uploading...");
        progressDialog.show();

        StorageReference reference = storageReference.child("StudentsData/"+System.currentTimeMillis()+".pdf");
        reference.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String fileName = uri.toString();
                                String studentName = binding.name.getText().toString().trim();
                                String studentRollNo = binding.rollNumber.getText().toString().trim();

                                StudentClassModel model = new StudentClassModel(fileName,studentName,studentRollNo);

                                databaseReference.child(databaseReference.push().getKey()).setValue(model);
                                progressDialog.dismiss();

                                Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_SHORT).show();
                                binding.filelogo.setVisibility(View.INVISIBLE);
                                binding.cancelfile.setVisibility(View.INVISIBLE);
                                binding.imagebrowse.setVisibility(View.VISIBLE);
                                binding.name.setText("");
                                binding.rollNumber.setText("");

                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded "+(int)percent+"%");
                    }
                });

    }


}