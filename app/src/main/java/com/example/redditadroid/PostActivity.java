package com.example.redditadroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.redditadroid.model.Comment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {
    private EditText commentAdd;
    private ImageButton sendComment;
    String postId;
    String user;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        commentAdd=(EditText) findViewById(R.id.commentText);
        sendComment=(ImageButton) findViewById(R.id.sendComment);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        user = "guest";


        if(currentUser != null){
            user = currentUser.getUid();
        }


        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();

            }
        });


    }

    private void postComment() {
        String comment = commentAdd.getText().toString();
        if(TextUtils.isEmpty(comment)){
            Toast.makeText(this, "Comment is empty", Toast.LENGTH_LONG).show();
            return;
        }
        Calendar calendar = Calendar.getInstance();
        String dateNow = DateFormat.getDateInstance().format(calendar.getTime());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("comments");
        String id = reference.push().getKey();
        Comment comment1 = new Comment(user,comment,postId,dateNow);


        reference.child(id).setValue(comment1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(PostActivity.this, "Comment added", Toast.LENGTH_LONG).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostActivity.this, "oh nouz", Toast.LENGTH_LONG).show();

            }
        });


    }
}