package com.example.redditadroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditadroid.model.Post;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    Context context;
    ArrayList<Post> list;

    public PostAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.post,parent,false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        Post post = list.get(position);
        holder.title.setText(post.getTitle());
        holder.text.setText(post.getText());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        TextView title,text;

        public PostViewHolder(@NonNull View postView){
            super(postView);
            title = postView.findViewById(R.id.postTitle);
            text = postView.findViewById(R.id.postText);
        }
    }
}
