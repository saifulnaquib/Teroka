package com.example.teroka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teroka.Adapter.PostAdapter;
import com.example.teroka.Model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AvatarActivity extends AppCompatActivity{

    ImageView backButton;

    private ImageView review, reviewList;

    private RecyclerView recyclerView;
    private ProgressBar progress_circular;

    private PostAdapter postAdapter;
    private List<Post> postList;

    private String place = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        review = findViewById(R.id.click_review);
        backButton = findViewById(R.id.back_button);

        progress_circular = findViewById(R.id.progress_circular);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AvatarActivity.this, PenangActivity.class);
                startActivity(intent);
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AvatarActivity.this, ReviewActivity.class);
                startActivity(intent);
            }
        });

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(AvatarActivity.this, postList);
        recyclerView.setAdapter(postAdapter);

        if (getIntent().getExtras() != null){
            place = getIntent().getStringExtra("place");
            //getSupportActionBar().setTitle(place);

            readPosts();
        }

        //readReviewsPosts();

    }

    private void readPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("reviews posts");
        Query query = reference.orderByChild("place").equalTo(place);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    postList.add(post);
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AvatarActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
/*
    private void readReviewsPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("reviews posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    postList.add(post);
                }

                postAdapter.notifyDataSetChanged();
                progress_circular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HinActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/

}
