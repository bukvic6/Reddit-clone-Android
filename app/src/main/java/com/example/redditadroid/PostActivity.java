package com.example.redditadroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.example.redditadroid.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {
    private EditText commentAdd;
    private ImageButton sendComment;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reactionReference  = db.getReference("ReactionComment");
    ArrayList<Comment> list;
    private TextView textPost,titlePost, creationDatePost,karmaPost;
    String postId;
    String user;
    private FirebaseAuth auth;
    private DatabaseReference postRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        commentAdd=(EditText) findViewById(R.id.commentText);
        sendComment=(ImageButton) findViewById(R.id.sendComment);
        textPost = findViewById(R.id.postText);
        creationDatePost = findViewById(R.id.creationDate);
        titlePost = findViewById(R.id.postTitle);
        karmaPost = findViewById(R.id.reaction);
        recyclerView = findViewById(R.id.commentsView);

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
        postRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                if(post != null){
                    String text = post.getText();
                    String title = post.getTitle();
                    String karma = post.getReaction();
                    String creationDate = post.getCreationDate();
                    textPost.setText(text);
                    titlePost.setText(title);
                    karmaPost.setText(karma);
                    creationDatePost.setText(creationDate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        loadComments();

    }

    private void postComment() {
        if(user.equals("guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Please login");
            builder.setMessage("If you want to use this function you need to log in first");
            builder.show();

        } else{
            String comment = commentAdd.getText().toString();
            if(TextUtils.isEmpty(comment)){
                Toast.makeText(this, "Comment is empty", Toast.LENGTH_LONG).show();
                return;
            }
            Calendar calendar = Calendar.getInstance();
            String dateNow = DateFormat.getDateInstance().format(calendar.getTime());
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("comments");
            String id = reference.push().getKey();
            String reaction = "1";
            Comment comment1 = new Comment(id,user,comment,postId,dateNow, reaction);
            DatabaseReference postCheat = FirebaseDatabase.getInstance().getReference("commCheat").child(user).child("comments");

            String idCheat = postCheat.push().getKey();
            Comment commentcheat = new Comment(reaction);
            postCheat.child(idCheat).setValue(commentcheat).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(PostActivity.this, "added", Toast.LENGTH_LONG).show();
                }
            });


            reference.child(id).setValue(comment1).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    reactionReference.child(id).child(user).setValue("UPVOTE");
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

    private void loadComments() {
        recyclerView = findViewById(R.id.commentsView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("comments");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    list.add(comment);
                    commentAdapter = new CommentAdapter(PostActivity.this,list,user);
                    recyclerView.setAdapter(commentAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}