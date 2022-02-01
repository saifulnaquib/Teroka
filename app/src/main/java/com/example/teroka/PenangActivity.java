package com.example.teroka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.teroka.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PenangActivity extends AppCompatActivity{

    FirebaseAuth mAuth;
    Button btnHin;
    //Button btnDest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penang);

        mAuth = FirebaseAuth.getInstance();
        btnHin = findViewById(R.id.explore_hin);
        //btnDest2 = findViewById(R.id.button9);

        btnHin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PenangActivity.this, Dest1Activity.class);
                startActivity(intent);
            }
        });

    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            startActivity(new Intent(PenangActivity.this, MainActivity.class));
        }

    }
}
