package com.example.redditadroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditadroid.model.Comment;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Post2postAdapter extends RecyclerView.Adapter<Post2postAdapter.Post2PostHolder> {

    Context context;
    List<Comment> list;

    public Post2postAdapter(Context context,List<Comment> list,String user){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public Post2PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.second_comment,parent,false);
        return new Post2PostHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Post2PostHolder holder, int position) {

        String commentUser = list.get(position).getUserId();
        String commentText = list.get(position).getText();
        holder.commentUser.setText(commentUser);
        holder.commentText.setText(commentText);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Post2PostHolder extends RecyclerView.ViewHolder{

        TextView commentUser,commentText;
        public Post2PostHolder(@NonNull View itemView){
            super(itemView);
            commentUser = itemView.findViewById(R.id.com2comName);
            commentText = itemView.findViewById(R.id.com2comText);
        }

    }
}
