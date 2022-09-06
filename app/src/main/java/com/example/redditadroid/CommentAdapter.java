package com.example.redditadroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    String user;
    private DatabaseReference reactCommentRef;
    private DatabaseReference commentRefs;
    boolean processLike = false;



    public CommentAdapter(Context context,List<Comment> list,String user){
        this.context = context;
        this.list = list;
        this.user = user;
        reactCommentRef = FirebaseDatabase.getInstance().getReference().child("ReactionComment");
        commentRefs = FirebaseDatabase.getInstance().getReference().child("Posts");
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
        String postId = list.get(position).getPostId();
        final String cId = list.get(position).getId();
        holder.commentUser.setText(commentUser);
        holder.commentText.setText(commentText);
        holder.commentCreationDate.setText(creationDate);
        setReaction(holder, cId);

        loadUserName(holder,commentUser);
        holder.upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.equals("guest")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("Please login");
                    builder.setMessage("If you want to use this function you need to log in first");
                    builder.show();
                }else {
                    final int pReaction = Integer.parseInt(list.get(holder.getAdapterPosition()).getReaction());
                    processLike = true;
                    final String commentId = list.get(holder.getAdapterPosition()).getId();
                    reactCommentRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (processLike) {
                                if (snapshot.child(postId).child(commentId).child(user).getValue() == "DOWNVOTE") {
                                    commentRefs.child(commentId).child("reaction").setValue("" + (pReaction + 2));
                                    reactCommentRef.child(commentId).child(user).setValue("UPVOTE");
                                    processLike = false;
                                } else if (snapshot.child(postId).child(commentId).child(user).getValue() == "UPVOTE") {
                                    commentRefs.child(commentId).child("reaction").setValue("" + (pReaction - 1));
                                    reactCommentRef.child(commentId).child(user).removeValue();
                                    processLike = false;
                                } else {
                                    commentRefs.child(commentId).child("reaction").setValue("" + (pReaction + 1));
                                    reactCommentRef.child(commentId).child(user).setValue("UPVOTE");
                                    processLike = false;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        holder.downVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.equals("guest")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("Please login");
                    builder.setMessage("If you want to use this function you need to log in first");
                    builder.show();
                }else {
                    final int pReaction = Integer.parseInt(list.get(holder.getAdapterPosition()).getReaction());
                    final String commentId = list.get(holder.getAdapterPosition()).getId();
                    processLike = true;
                    final String postId = list.get(holder.getAdapterPosition()).getId();
                    reactCommentRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(processLike){
                                if(snapshot.child(postId).child(commentId).child(user).getValue()== "DOWNVOTE"){
                                    commentRefs.child(postId).child("reaction").setValue("" +(pReaction+1));
                                    reactCommentRef.child(postId).child(user).removeValue();
                                    processLike = false;
                                }else if(snapshot.child(postId).child(user).getValue() == "UPVOTE"){
                                    commentRefs.child(postId).child("reaction").setValue("" +(pReaction-2));
                                    reactCommentRef.child(postId).child(user).setValue("DOWNVOTE");
                                    processLike = false;
                                }
                                else{
                                    commentRefs.child(postId).child("reaction").setValue("" +(pReaction-1));
                                    reactCommentRef.child(postId).child(user).setValue("DOWNVOTE");
                                    processLike = false;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }}
        });


    }
    private void setReaction(final CommentViewHolder holder, final String postId) {
        final String commentId = list.get(holder.getAdapterPosition()).getId();

        reactCommentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postId).child(commentId).child(user).getValue()== "DOWNVOTE"){
                    holder.downVote.setText("DownVoted");
                }else if(snapshot.child(postId).child(commentId).child(user).getValue()== "UPVOTE"){
                    holder.upVote.setText("Liked");
                }
                else {
                    holder.upVote.setText("Like");
                    holder.downVote.setText("DownVote");

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadUserName(final CommentViewHolder holder,String commentUser) {
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

            reaction = itemView.findViewById(R.id.reaction);
            upVote = itemView.findViewById(R.id.btnUpvote);
            downVote = itemView.findViewById(R.id.downVote);
            comm = itemView.findViewById(R.id.comments);
        }
    }

}
