package com.example.redditadroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditadroid.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    Context context;
    ArrayList<Post> list;
    private DatabaseReference reactionRefs;
    private DatabaseReference postRef;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();
    String user = currentUser.getUid();

    boolean processLike = false;


    public PostAdapter(Context context, ArrayList<Post> list) {
        this.context = context;
        this.list = list;
        reactionRefs = FirebaseDatabase.getInstance().getReference().child("Reaction");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");

    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.post,parent,false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {

        String title = list.get(position).getTitle();
        String text = list.get(position).getText();
        String reaction = list.get(position).getReaction();
        final String pId = list.get(position).getId();
        holder.text.setText(text);
        holder.title.setText(title);
        holder.reaction.setText(reaction);
        setReaction(holder, pId);

        holder.upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pReaction = Integer.parseInt(list.get(holder.getAdapterPosition()).getReaction());
                processLike = true;
                final String postId = list.get(holder.getAdapterPosition()).getId();
                reactionRefs.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(processLike){
                            if(snapshot.child(postId).child(user).getValue() == "DOWNVOTE"){
                                postRef.child(postId).child("reaction").setValue("" +(pReaction+2));
                                reactionRefs.child(postId).child(user).setValue("UPVOTE");
                                processLike = false;
                            }else if(snapshot.child(postId).child(user).getValue() == "UPVOTE"){
                                postRef.child(postId).child("reaction").setValue("" +(pReaction-1));
                                reactionRefs.child(postId).child(user).removeValue();
                                processLike = false;
                            }
                            else{
                                postRef.child(postId).child("reaction").setValue("" +(pReaction+1));
                                reactionRefs.child(postId).child(user).setValue("UPVOTE");
                                processLike = false;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                })
;            }
        });
        holder.downVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pReaction = Integer.parseInt(list.get(holder.getAdapterPosition()).getReaction());
                processLike = true;
                final String postId = list.get(holder.getAdapterPosition()).getId();
                reactionRefs.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(processLike){
                            if(snapshot.child(postId).child(user).getValue() == "DOWNVOTE"){
                                postRef.child(postId).child("reaction").setValue("" +(pReaction+1));
                                reactionRefs.child(postId).child(user).removeValue();
                                processLike = false;
                            }else if(snapshot.child(postId).child(user).getValue() == "UPVOTE"){
                                postRef.child(postId).child("reaction").setValue("" +(pReaction-2));
                                reactionRefs.child(postId).child(user).setValue("DOWNVOTE");
                                processLike = false;
                            }
                            else{
                                postRef.child(postId).child("reaction").setValue("" +(pReaction-1));
                                reactionRefs.child(postId).child(user).setValue("DOWNVOTE");
                                processLike = false;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    private void setReaction(final PostViewHolder holder,final String postId) {
        reactionRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postId).child(user).getValue() == "Dislike"){
                    holder.downVote.setText("DownVoted");
                }else if(snapshot.child(postId).child(user).getValue() == "Liked"){
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

    @Override
    public int getItemCount() {
        return list.size();
    }

     class PostViewHolder extends RecyclerView.ViewHolder{
        TextView title,text, reaction;
        Button upVote,downVote;

        public PostViewHolder(@NonNull View postView){
            super(postView);
            title = postView.findViewById(R.id.postTitle);
            text = postView.findViewById(R.id.postText);
            reaction = postView.findViewById(R.id.reaction);
            upVote = postView.findViewById(R.id.btnUpvote);
            downVote = postView.findViewById(R.id.downVote);
        }
    }
}
