package com.example.teroka.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.teroka.CommentsActivity;
import com.example.teroka.Model.RPost;
import com.example.teroka.Model.User;
import com.example.teroka.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder>{

    public Context mContext;
    public List<RPost> mPostList;
    private FirebaseUser firebaseUser;

    public RecommendAdapter(Context mContext, List<RPost> mPostList) {
        this.mContext = mContext;
        this.mPostList = mPostList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recommend_retrieved_layout, parent, false);
        return new RecommendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final RPost post = mPostList.get(position);
        if (post.getRecommendimage() == null){
            holder.reviewImage.setVisibility(View.GONE);
        }
        holder.reviewImage.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(post.getRecommendimage()).into(holder.reviewImage);
        holder.expandable_text.setText(post.getRecommendation());
        holder.placeTextView.setText(post.getPlace());
        holder.reviewOnTextView.setText(post.getDate());

        publisherInformation(holder.review_by_textview, post.getPublisher());
        isLiked(post.getPostid(), holder.like);
        isDisliked(post.getPostid(), holder.dislike);
        getLikes(holder.likes, post.getPostid());
        getDislikes(holder.dislikes, post.getPostid());

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.like.getTag().equals("Rlike") && holder.dislike.getTag().equals("Rdislike")){
                    FirebaseDatabase.getInstance().getReference().child("Rlikes").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                }
                else if (holder.like.getTag().equals("Rlike") && holder.dislike.getTag().equals("Rdisliked")){
                    FirebaseDatabase.getInstance().getReference().child("Rdislikes").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Rlikes").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Rlikes").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.dislike.getTag().equals("Rdislike") && holder.like.getTag().equals("Rlike")){
                    FirebaseDatabase.getInstance().getReference().child("Rdislikes").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                }else if (holder.dislike.getTag().equals("Rdislike") && holder.like.getTag().equals("Rliked")){
                    FirebaseDatabase.getInstance().getReference().child("Rlikes").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Rdislikes").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Rdislikes").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postid",post.getPostid());
                intent.putExtra("publisher", post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtra("postid",post.getPostid());
                intent.putExtra("publisher", post.getPublisher());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView review_by_textview, likes, dislikes, comments;
        public ImageView more, reviewImage, like, dislike, comment, save;
        public TextView placeTextView, reviewOnTextView;
        public ExpandableTextView expandable_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            review_by_textview = itemView.findViewById(R.id.review_by_textview);
            likes = itemView.findViewById(R.id.likes);
            dislikes = itemView.findViewById(R.id.dislikes);
            comments = itemView.findViewById(R.id.comments);
            //more = itemView.findViewById(R.id.more);
            reviewImage = itemView.findViewById(R.id.reviewImage);
            like = itemView.findViewById(R.id.like);
            dislike = itemView.findViewById(R.id.dislike);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            placeTextView = itemView.findViewById(R.id.placeTextView);
            reviewOnTextView = itemView.findViewById(R.id.reviewOnTextView);
            expandable_text = itemView.findViewById(R.id.expand_text_view);

        }
    }

    private void publisherInformation(TextView recommendby, String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                recommendby.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void isLiked(String postid, ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Rlikes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("Rliked");
                }
                else {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("Rlike");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isDisliked(String postid, ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Rdislikes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_disliked);
                    imageView.setTag("Rdisliked");
                }
                else {
                    imageView.setImageResource(R.drawable.ic_dislike);
                    imageView.setTag("Rdislike");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLikes(TextView likes, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Rlikes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long numberofLikes = snapshot.getChildrenCount();
                int NOL = (int) numberofLikes;
                if (NOL > 1){
                    likes.setText(snapshot.getChildrenCount()+ "likes");
                }else if (NOL == 0){
                    likes.setText("0 likes");
                }else {
                    likes.setText(snapshot.getChildrenCount()+ "like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDislikes(TextView dislikes, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Rdislikes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long numberofDislikes = snapshot.getChildrenCount();
                int NOD = (int) numberofDislikes;
                if (NOD > 1){
                    dislikes.setText(snapshot.getChildrenCount()+ "dislikes");
                }else if (NOD == 0){
                    dislikes.setText("0 dislikes");
                }else {
                    dislikes.setText(snapshot.getChildrenCount()+ "dislike");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
