package com.example.teroka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.teroka.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button btnLogout;
    Button btnFunction1;
    Button btnFunction2;
    Button btnFunction3;
    Button btnFunction4;
    Button btnPenang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);
        btnFunction1 = findViewById(R.id.button2);
        btnFunction2 = findViewById(R.id.button3);
        btnFunction3 = findViewById(R.id.button4);
        btnFunction4 = findViewById(R.id.button5);
        btnPenang = findViewById(R.id.button7);
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
            startActivity(new Intent(MainActivity.this, LogSign.class));
        }

    }

    public void function2(View view) {

        Intent intent = new Intent(MainActivity.this, SuggestPlaceActivity.class);
        startActivity(intent);
    }

    public void function3(View view) {

        Intent intent = new Intent(MainActivity.this, PlannerActivity.class);
        startActivity(intent);
    }

    public void function4(View view) {

        Intent intent = new Intent(MainActivity.this, RecommendationActivity.class);
        startActivity(intent);
    }

    public void function5(View view) {

        Intent intent = new Intent(MainActivity.this, PenangActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LogSign.class);
        startActivity(intent);
    }

}