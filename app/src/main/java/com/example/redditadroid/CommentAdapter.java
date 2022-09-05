package com.example.redditadroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditadroid.model.Comment;
import com.example.redditadroid.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    Context context;
    List<Comment> list;
    private DatabaseReference postRef;

    public CommentAdapter(Context context,List<Comment> list){
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment,parent,false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        String commentUser = list.get(position).getUserId();
        String commentText = list.get(position).getText();
        String creationDate = list.get(position).getCreationDate();
        final String cId = list.get(position).getId();
        holder.commentUser.setText(commentUser);
        holder.commentText.setText(commentText);
        holder.commentCreationDate.setText(creationDate);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference("users").child(commentUser);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    String username = userProfile.username;
                    String displayName = userProfile.displayName;
                    if(displayName.equals("")){
                        holder.commentUser.setText(username);
                    }else {
                        holder.commentUser.setText(displayName);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder{
        TextView commentUser,commentText,commentCreationDate, reaction;
        Button upVote,downVote, comm;

        public CommentViewHolder(@NonNull View itemView){
            super(itemView);
            commentUser = itemView.findViewById(R.id.commentName);
            commentText = itemView.findViewById(R.id.commentText);
            commentCreationDate = itemView.findViewById(R.id.creationDatecomment);

//            reaction = postView.findViewById(R.id.reaction);
//            upVote = postView.findViewById(R.id.btnUpvote);
//            downVote = postView.findViewById(R.id.downVote);
//            comm = postView.findViewById(R.id.comments);
        }
    }

}
