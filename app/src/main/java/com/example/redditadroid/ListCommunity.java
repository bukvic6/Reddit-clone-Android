package com.example.redditadroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.redditadroid.model.Community;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListCommunity extends AppCompatActivity implements RecViewInterface{
    RecyclerView recyclerView;
    DatabaseReference database;
    CommunityAdapter communityAdapter;
    ArrayList<Community> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_community);

        recyclerView = findViewById(R.id.communityList);
        database = FirebaseDatabase.getInstance("https://redditadroid-default-rtdb.firebaseio.com/").getReference("Communities");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        communityAdapter = new CommunityAdapter(this, list, this);
        recyclerView.setAdapter(communityAdapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Community community = dataSnapshot.getValue(Community.class);
                    list.add(community);
                }
                communityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ListCommunity.this, CommunityActivity.this);

    }
}