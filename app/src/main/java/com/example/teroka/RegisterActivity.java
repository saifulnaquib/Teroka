package com.example.teroka;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teroka.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ImageView profileImage;
    private EditText email, password, username;
    private Button btnRegister;
    private TextView textLogin;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private ProgressDialog loader;
    private String onlineUserID = "";
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.register_username);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        btnRegister = findViewById(R.id.register);
        textLogin = findViewById(R.id.text_login);

        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void Register() {
        String userName = username.getText().toString().trim();
        String user = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (userName.isEmpty()) {
            username.setError("Username cannot be empty");
        }
        if (user.isEmpty()) {
            email.setError("Email cannot be empty");
        }
        if (pass.isEmpty()) {
            password.setError("Password cannot be empty");
        } else {
            loader.setMessage("Registration in progress");
            loader.setCanceledOnTouchOutside(false);
            loader.show();

            mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        onlineUserID = mAuth.getCurrentUser().getUid();
                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(onlineUserID);
                        Map hashMap = new HashMap();
                        hashMap.put("username", userName);
                        hashMap.put("id", onlineUserID);
                        hashMap.put("email", user);
                        reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Failed to register" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }

                                finish();
                                loader.dismiss();

                            }
                        });
                        final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile images").child(onlineUserID);
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), resultUri);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20,byteArrayOutputStream);
                        byte[] data = byteArrayOutputStream.toByteArray();
                        UploadTask uploadTask = filePath.putBytes(data);

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                if (taskSnapshot.getMetadata().getReference() !=null){
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUri = uri.toString();
                                            Map hashMap = new HashMap();
                                            hashMap.put("profileimageuri", imageUri);
                                            reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(RegisterActivity.this, "Profile image added successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        Toast.makeText(RegisterActivity.this, "Process failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            finish();
                                        }
                                    });
                                }
                            }
                        });

                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        loader.dismiss();
                        return;
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            resultUri=data.getData();
            profileImage.setImageURI(resultUri);
        }
        else{
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}

