package com.example.redditadroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redditadroid.model.Post;
import com.example.redditadroid.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private TextView greetingTextView,emailView,usernameView,karmaView;
    Button button;
    ActionBar actionBar;
    MyPostAdapter postAdapter;
    ArrayList<Post> list;
    RecyclerView recyclerView;
    DatabaseReference database;
    private DatabaseReference postRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String user = currentUser.getUid();
        greetingTextView = findViewById(R.id.greeting);
        emailView = findViewById(R.id.emailAddress);
        usernameView = findViewById(R.id.username);
        karmaView = findViewById(R.id.karma);


        // RECYCLER VIEW
        recyclerView = findViewById(R.id.my_posts);
        database = FirebaseDatabase.getInstance("https://redditadroid-default-rtdb.firebaseio.com/").getReference("Posts");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();

        loadPosts(user);

        // BOTTOM BAR
        actionBar = getSupportActionBar();
        BottomNavigationView navigationView = findViewById(R.id.navigationProfile);
        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.my_communities:
                    Intent i = new Intent(ProfileActivity.this, UserCommunity.class);
                    startActivity(i);
                    break;
                case R.id.update:
                    Intent f = new Intent(ProfileActivity.this, UpdateProfile.class);
                    startActivity(f);
                    break;
            }
            return true;


        });

        // ADD VALUE TO TEXTVIEW

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        postRef.orderByChild("userId").equalTo(currentUser.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum = 0;

                for(DataSnapshot data: snapshot.getChildren()){
                    String value = data.child("reaction").getValue(String.class);
                    assert value != null;
                    int total = Integer.parseInt(value);
                    sum = sum +total;

                }
                karmaView.setText(String.valueOf(sum));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    String email = userProfile.email;
                    String username = userProfile.username;
                    greetingTextView.setText(userProfile.username);
                    emailView.setText(email);
                    usernameView.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this,"something wrong happened", Toast.LENGTH_LONG).show();

            }
        });

    }
    private void loadPosts(String id) {
        Query query = database.orderByChild("userId").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    list.add(post);
                }
                postAdapter = new MyPostAdapter(ProfileActivity.this, list);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}