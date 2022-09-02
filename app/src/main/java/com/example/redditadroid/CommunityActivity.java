package com.example.redditadroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redditadroid.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class CommunityActivity extends AppCompatActivity {
    PostAdapter postAdapter;
    ArrayList<Post> list;
    RecyclerView recyclerView;
    DatabaseReference database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        String name = getIntent().getStringExtra("NAME");
        String id = getIntent().getStringExtra("ID");

        TextView nametext = findViewById(R.id.CommName);
        nametext.setText(name);
        recyclerView = findViewById(R.id.postList);
        database = FirebaseDatabase.getInstance("https://redditadroid-default-rtdb.firebaseio.com/").getReference("Posts");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        loadPosts(id);

        Button createPost = findViewById(R.id.addPostButton);
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CommunityActivity.this, AddActivity.class);
                i.putExtra("ID", id);
                startActivity(i);
            }
        });



    }
    private void loadPosts(String id) {
        Query query = database.orderByChild("communityId").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    list.add(post);
                }
                postAdapter = new PostAdapter(CommunityActivity.this, list);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}