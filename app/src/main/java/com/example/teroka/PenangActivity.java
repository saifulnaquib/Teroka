package com.example.teroka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PenangActivity extends AppCompatActivity{

    FirebaseAuth mAuth;
    Button btnHin, btnAvatar, btnKampong, btnAtv;
    //Button btnDest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penang);

        mAuth = FirebaseAuth.getInstance();
        btnHin = findViewById(R.id.explore_hin);
        btnAvatar = findViewById(R.id.explore_avatar);
        btnKampong = findViewById(R.id.explore_kampong);
        btnAtv = findViewById(R.id.explore_atv);
        BottomNavigationView bnv = findViewById(R.id.bottom_navigation);
        //btnDest2 = findViewById(R.id.button9);

        btnHin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PenangActivity.this, HinActivity.class);
                intent.putExtra("place", "Hin Bus Depot");
                startActivity(intent);
            }
        });

        btnAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PenangActivity.this, AvatarActivity.class);
                intent.putExtra("place", "Avatar Secret Garden");
                startActivity(intent);
            }
        });

        btnKampong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PenangActivity.this, KampongActivity.class);
                intent.putExtra("place", "Kampong Agong");
                startActivity(intent);
            }
        });

        btnAtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PenangActivity.this, ATV.class);
                intent.putExtra("place", "Penang ATV Eco Balik Pulau");
                startActivity(intent);
            }
        });

        //Set Home selected
        bnv.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.suggestion:
                        startActivity(new Intent(getApplicationContext(),RecommendationActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(),MapsActivity.class));
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

    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            startActivity(new Intent(PenangActivity.this, MainActivity.class));
        }

    }
}
