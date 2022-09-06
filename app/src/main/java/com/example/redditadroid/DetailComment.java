package com.example.redditadroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redditadroid.model.Comment;
import com.example.redditadroid.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xml.sax.DTDHandler;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DetailComment extends AppCompatActivity {
    String commentId,postId;
    RecyclerView recyclerView;
    Post2postAdapter commentAdapter;
    ArrayList<Comment> list;
    private TextView commentUser,commentcreatDate, commenttext;
    private FirebaseAuth auth;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_comment);
        Intent intent = getIntent();
        commentId = intent.getStringExtra("commentId");
        postId = intent.getStringExtra("postId");
        commentUser = findViewById(R.id.commentUser);
        commentcreatDate = findViewById(R.id.commentDate);
        commenttext = findViewById(R.id.commentTextd);



        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        user = "guest";
        if(currentUser != null){
            user = currentUser.getUid();
        }
        loadComments();

    }
    private void loadComments() {
        recyclerView = findViewById(R.id.commentsView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("comments").child(commentId).child("com2com");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    list.add(comment);
                    commentAdapter = new Post2postAdapter(DetailComment.this,list,user);
                    recyclerView.setAdapter(commentAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}