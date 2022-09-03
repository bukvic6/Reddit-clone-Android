package com.example.redditadroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.redditadroid.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UpdatePostActivity extends AppCompatActivity {
    private EditText titleEdit, textEdit;
    private String titleText, textText ,textReaction, CommunityId, UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);

        Intent intent = getIntent();
        String postId = intent.getStringExtra("postId");


        titleEdit = findViewById(R.id.updatePostTitle);
        textEdit = findViewById(R.id.updatePostText);
        showData(postId);
        Button editPost = findViewById(R.id.editPostButton);
        editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePost(postId);
            }
        });
        
    }

    private void updatePost(String postId) {
        if(TextUtils.isEmpty(titleText)){
            Toast.makeText(UpdatePostActivity.this, "Please enter post title", Toast.LENGTH_LONG).show();
            titleEdit.setError("Username is required");
            titleEdit.requestFocus();
        }
        else if (TextUtils.isEmpty(textText)){
            Toast.makeText(UpdatePostActivity.this, "Please Enter email", Toast.LENGTH_LONG).show();
            textEdit.setError("Text is required");
            textEdit.requestFocus();
        } else {
            titleText = titleEdit.getText().toString();
            textText = textEdit.getText().toString();
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference reference = database.getReference("Posts");
            Post write = new Post(postId,titleText,textText,UserId,CommunityId,textReaction);
            Map<String, Object> map = new HashMap<>();
            map.put("id", postId);
            map.put("title", titleText);
            map.put("text", textText);
            map.put("userId", UserId);
            map.put("communityId", CommunityId);
            map.put("reaction", textReaction);

            reference.child(postId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(UpdatePostActivity.this, "Success!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdatePostActivity.this, "Error", Toast.LENGTH_LONG).show();

                }
            });


        }
    }

    private void showData(String postId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Posts").child(postId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post editPost = snapshot.getValue(Post.class);
                titleText = editPost.getTitle();
                textText = editPost.getText();
                textReaction = editPost.getReaction();
                CommunityId = editPost.getCommunityId();
                UserId = editPost.getUserId();

                titleEdit.setText(titleText);
                textEdit.setText(textText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdatePostActivity.this,"something wrong happened", Toast.LENGTH_LONG).show();

            }
        });
    }
}