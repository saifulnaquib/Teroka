package com.example.teroka;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.teroka.ui.login.LoginActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class ReviewActivity extends AppCompatActivity{

    private Spinner spinner;
    private EditText reviewBox;
    private ImageView imageView;
    private Button cancelBtn, postReviewBtn;

    //private String postedByName = "";
    private DatabaseReference postedbyRef;
    private ProgressDialog loader;
    private String myUrl = "";
    StorageTask uploadTask;
    StorageReference storageReference;
    private Uri imageuri;

    private FirebaseAuth mauth;
    private FirebaseUser mUser;
    private String onlineUserID = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        spinner = findViewById(R.id.spinner);
        reviewBox = findViewById(R.id.review_text);
        imageView = findViewById(R.id.imageView3);
        cancelBtn = findViewById(R.id.button10);
        postReviewBtn = findViewById(R.id.button11);

        loader = new ProgressDialog(this);
        mauth = FirebaseAuth.getInstance();
        mUser = mauth.getCurrentUser();
        onlineUserID = mUser.getUid();

        /*postedbyRef = FirebaseDatabase.getInstance().getReference().child(onlineUserID);
        postedbyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postedByName = snapshot.child("fullname").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        storageReference = FirebaseStorage.getInstance().getReference("reviews");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.place));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().equals("select place")){
                    Toast.makeText(ReviewActivity.this, "Please select a valid place", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performValidations();
            }
        });


    }

    String getReviewText(){
        return reviewBox.getText().toString().trim();
    }

    String getPlace(){
        return spinner.getSelectedItem().toString();
    }

    String mDate = DateFormat.getDateInstance().format(new Date());
    DatabaseReference ref = FirebaseDatabase.getInstance("https://teroka-3752b-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("reviews posts");

    private void performValidations() {
        if (getReviewText().isEmpty()){
            reviewBox.setError("Reviews Required!");
        }
        else if (getPlace().equals("select place")){
            Toast.makeText(this, "Select a valid place", Toast.LENGTH_SHORT).show();
        }
        else if (!getReviewText().isEmpty() && !getPlace().equals("") && imageuri == null){
            uploadAReviewwithNoImage();
        }
        else if (!getReviewText().isEmpty() && !getPlace().equals("") && imageuri != null){
            uploadAReviewWithImage();
        }
    }

    private void startLoader(){
        loader.setMessage("Uploading your review");
        loader.setCanceledOnTouchOutside(false);
        loader.show();
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadAReviewwithNoImage(){
        startLoader();
        String postid = ref.push().getKey();

        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("postid", postid);
        hashmap.put("review", getReviewText());
        hashmap.put("publisher", onlineUserID);
        hashmap.put("place", getPlace());
        //hashmap.put("reviewby", postedByName);
        hashmap.put("date",mDate);

        ref.child(postid).setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ReviewActivity.this, "Review Posted Successfully", Toast.LENGTH_SHORT).show();
                    loader.dismiss();
                    startActivity(new Intent(ReviewActivity.this, Dest1Activity.class));
                    finish();
                }
                else{
                    Toast.makeText(ReviewActivity.this, "Could not upload image" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    loader.dismiss();
                }
            }
        });
    }

    private void uploadAReviewWithImage(){
        startLoader();
        final StorageReference fileReference;
        fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageuri));
        uploadTask = fileReference.putFile(imageuri);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isComplete()){
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Uri downloadUri = (Uri) task.getResult();
                    myUrl = downloadUri.toString();
                    String postid = ref.push().getKey();

                    HashMap<String, Object> hashmap = new HashMap<>();
                    hashmap.put("postid", postid);
                    hashmap.put("review", getReviewText());
                    hashmap.put("publisher", onlineUserID);
                    hashmap.put("place", getPlace());
                    //hashmap.put("reviewby", postedByName);
                    hashmap.put("reviewimage", myUrl);
                    hashmap.put("date",mDate);

                    ref.child(postid).setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ReviewActivity.this, "Review Posted Successfully", Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                                startActivity(new Intent(ReviewActivity.this, Dest1Activity.class));
                                finish();
                            }
                            else {
                                Toast.makeText(ReviewActivity.this, "Could not upload image" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            imageuri = data.getData();
            imageView.setImageURI(imageuri);
        }
    }
}
