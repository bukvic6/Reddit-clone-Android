package com.example.redditadroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.redditadroid.model.Community;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateCommunity extends AppCompatActivity {
    private EditText communityNameEdit, communityDescEdit, rulesEdit;
    private String commNameText, commDescText, rulesText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_community);
        Intent intent = getIntent();
        String communityId = intent.getStringExtra("communityId");
        communityNameEdit = findViewById(R.id.updateCommName);
        communityDescEdit = findViewById(R.id.updateCommunityDescription);
        rulesEdit = findViewById(R.id.updateCommunityDescription);
        showData(communityId);



    }

    private void showData(String communityId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Communities").child(communityId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Community editCommunity = snapshot.getValue(Community.class);
                commNameText = editCommunity.getName();
                commDescText = editCommunity.getDescription();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}