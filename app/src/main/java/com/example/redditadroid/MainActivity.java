package com.example.redditadroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.redditadroid.model.Post;
import com.example.redditadroid.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView usernameM;
    private FirebaseAuth auth;

    RecyclerView recyclerView;
    DatabaseReference database;
    PostAdapter postAdapter;
    ArrayList<Post> list;
    ActionBar actionBar;
    String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        user = "guest";


        if(currentUser != null){
            user = currentUser.getUid();
        }

        FirebaseDatabase databasef = FirebaseDatabase.getInstance();
        DatabaseReference reference = databasef.getReference("users").child(user);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null){
                    usernameM.setText("Username: " + user.username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //ACTION BAR
        actionBar = getSupportActionBar();
        actionBar.setTitle("Home");
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_comm:
                    if(user.equals("guest")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("Please login");
                        builder.setMessage("If you want to use this function you need to log in first");
                        builder.show();
                    }else {
                        Intent i = new Intent(MainActivity.this, AddCommunityActivity.class);
                        startActivity(i);
                    }
                    break;
                case R.id.profile:
                    if(user.equals("guest")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("Please login");
                        builder.setMessage("If you want to use this function you need to log in first");
                        builder.show();
                    }else {
                    Intent f = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(f);
                    }
                    break;
                case R.id.home:
                    Intent l = new Intent(MainActivity.this, ListCommunity.class);
                    startActivity(l);
                    break;
            }
            return true;


        });
        //TOP BAR

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                user = "";
                Intent i = new Intent(MainActivity.this, Login_form.class);
                startActivity(i);
            }
        });

        Button createCommunity = findViewById(R.id.addCommunity);
        createCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddCommunityActivity.class);
                startActivity(i);            }
        });
        Button userProfile = findViewById(R.id.userProfile);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);            }
        });
        recyclerView = findViewById(R.id.postList);
        database = FirebaseDatabase.getInstance("https://redditadroid-default-rtdb.firebaseio.com/").getReference("Posts");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        loadPosts(user);
        usernameM = findViewById(R.id.mainUsername);


    }

    private void loadPosts(String user) {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    list.add(post);
                }
                postAdapter = new PostAdapter(MainActivity.this, list,user);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }


}