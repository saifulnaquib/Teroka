package com.example.teroka;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teroka.Adapter.PostAdapter;
import com.example.teroka.Adapter.RecommendAdapter;
import com.example.teroka.Model.RPost;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecommendationActivity extends AppCompatActivity {

    private TextView recommend, reviewList;

    private RecyclerView recyclerView;
    private ProgressBar progress_circular;

    private RecommendAdapter recommendAdapter;
    private List<RPost> postList;

    private String place = "";

    FirebaseAuth mAuth;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        mAuth = FirebaseAuth.getInstance();
        BottomNavigationView bnv = findViewById(R.id.bottom_navigation);

        recommend = findViewById(R.id.click_review);
        progress_circular = findViewById(R.id.progress_circular);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecommendationActivity.this, PostRecommendationActivity.class);
                startActivity(intent);
            }
        });

        postList = new ArrayList<>();
        recommendAdapter = new RecommendAdapter(RecommendationActivity.this, postList);
        recyclerView.setAdapter(recommendAdapter);

        readRecommendationPosts();

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

    private void readRecommendationPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("recommendations posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RPost post = dataSnapshot.getValue(RPost.class);
                    postList.add(post);
                }

                recommendAdapter.notifyDataSetChanged();
                progress_circular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecommendationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
