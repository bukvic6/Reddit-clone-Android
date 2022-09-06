package com.example.redditadroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.example.redditadroid.model.Rules;
import com.example.redditadroid.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth auth;
    String user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        String name = getIntent().getStringExtra("NAME");
        String id = getIntent().getStringExtra("ID");
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser == null){
//            Intent intent = new Intent(this, Login_form.class);
//            startActivity(intent);
//            finish();
//            return;
//        }
        user = "guest";
        if (currentUser != null){
            user = currentUser.getUid();
        }

        TextView nametext = findViewById(R.id.CommName);
        nametext.setText(name);
        TextView communityRules = findViewById(R.id.communityRules);



        //VALUE TO RULES

        FirebaseDatabase rules = FirebaseDatabase.getInstance();

        DatabaseReference reference = rules.getReference("Rules").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Rules rules = snapshot.getValue(Rules.class);
                if(rules != null){
                    String desc = rules.getDescription();
                    communityRules.setText(desc);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CommunityActivity.this,"something wrong happened", Toast.LENGTH_LONG).show();

            }
        });

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
                if(user.equals("guest")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CommunityActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Please login");
                    builder.setMessage("If you want to use this function you need to log in first");
                    builder.show();
                }else {
                    Intent i = new Intent(CommunityActivity.this, AddActivity.class);
                    i.putExtra("ID", id);
                    startActivity(i);
                }
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
                postAdapter = new PostAdapter(CommunityActivity.this, list,user);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

}