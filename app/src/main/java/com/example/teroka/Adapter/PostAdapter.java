package com.example.teroka.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.teroka.Model.Post;
import com.example.teroka.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    public Context mContext;
    public List<Post> mPostList;

    public PostAdapter(Context mContext, List<Post> mPostList) {
        this.mContext = mContext;
        this.mPostList = mPostList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_retrieved_layout, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Post post = mPostList.get(position);
        if (post.getReviewimage() == null){
            holder.reviewImage.setVisibility(View.GONE);
        }
        holder.reviewImage.setVisibility(View.VISIBLE);
        Glide.with(mContext).load(post.getReviewimage()).into(holder.reviewImage);
        holder.expandable_text.setText(post.getReview());
        holder.placeTextView.setText(post.getPlace());
        holder.reviewOnTextView.setText(post.getDate());

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
            more = itemView.findViewById(R.id.more);
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

}
