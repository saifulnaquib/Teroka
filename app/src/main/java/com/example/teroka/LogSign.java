package com.example.teroka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teroka.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogSign extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    TextView create_acc;
    private Button login_button;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_sign);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user!=null){
                    Intent intent = new Intent(LogSign.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(LogSign.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        };

        create_acc = (TextView)findViewById(R.id.create_acc);
        login_button = (Button)findViewById(R.id.login_button);

        //Set button alpha to zero
        login_button.setAlpha(0f);
        login_button.setTranslationY(50);

        //Animate the alpha value to 1f and set duration as 1.5 secs.
        login_button.animate().alpha(1f).translationYBy(-50).setDuration(1000);

        login_button.setOnClickListener(this);
        create_acc.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                openLoginActivity();
                break;
            case R.id.create_acc:
                openRegisterActivity();
                break;
        }
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openRegisterActivity() {
        Intent intent_register = new Intent(this, RegisterActivity.class);
        startActivity(intent_register);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}