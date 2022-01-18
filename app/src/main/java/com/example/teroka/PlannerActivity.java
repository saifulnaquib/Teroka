package com.example.teroka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PlannerActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        mAuth = FirebaseAuth.getInstance();
        //btnLogout = findViewById(R.id.button7);
/*
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });*/
    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            startActivity(new Intent(PlannerActivity.this, MainActivity.class));
        }

    }

    public void logout(View view) {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(PlannerActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
