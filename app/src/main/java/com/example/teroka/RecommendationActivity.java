package com.example.teroka;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RecommendationActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        mAuth = FirebaseAuth.getInstance();
        BottomNavigationView bnv = findViewById(R.id.bottom_navigation);

        //Set Home selected
        bnv.setSelectedItemId(R.id.suggestion);

        //Perform ItemSelectedListener
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.suggestion:
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(),SuggestPlaceActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.planner:
                        startActivity(new Intent(getApplicationContext(),PlannerActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),LogSign.class));
                        return true;
                }
                return false;
            }
        });
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
            startActivity(new Intent(RecommendationActivity.this, MainActivity.class));
        }

    }

    public void logout(View view) {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(RecommendationActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
