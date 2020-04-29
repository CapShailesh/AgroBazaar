package com.example.sdlproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class NewCropActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private Button mChooseButton, mUploadButton;
    private ImageView mImageView;
    private EditText product_name;
    private TextView category;

    private Uri uri;

    private StorageReference mStorageRef;
    private FirebaseFirestore db;
    CollectionReference noteref;


    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_crop);


        spinner=(Spinner) findViewById(R.id.spinner);
        adapter=ArrayAdapter.createFromResource(this,R.array.names,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        mChooseButton = findViewById(R.id.upload_image_crop);
        mImageView = findViewById(R.id.product_img);
        product_name = findViewById(R.id.pro_name);
        mUploadButton = findViewById(R.id.submit_btn);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        db = FirebaseFirestore.getInstance();
        noteref = db.collection("CropImage");

        mChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Toast.makeText(getBaseContext(),parent.getItemIdAtPosition(position),+ "Selected",Toast.LENGTH_LONG).show();
                ((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " Selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(NewCropActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if(uri!=null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+ "." + getFileExtension(uri));
            mUploadTask = fileReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(NewCropActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            Upload upload = new Upload(product_name.getText().toString().trim(),downloadUrl.toString(), spinner.getSelectedItem().toString());
                            noteref.add(upload);
                            startActivity(new Intent(NewCropActivity.this, HomeActivity.class));
                            finish();
                            //String uploadId = mDatabaseRef.push().getKey();
//                            notre.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) throws NullPointerException {
//
                                Toast.makeText(NewCropActivity.this, "No File Selected", Toast.LENGTH_SHORT).show();
//                            }catch (NullPointerException npe){}
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData()!=null);
        try {
            uri = data.getData();
        }catch (NullPointerException ne){
            Toast.makeText(this, "File not selected", Toast.LENGTH_SHORT).show();
        }
        Picasso.with(this).load(uri).into(mImageView);

    }
}
