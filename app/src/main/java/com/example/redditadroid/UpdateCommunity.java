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

import com.example.redditadroid.model.Community;
import com.example.redditadroid.model.Rules;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

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
        rulesEdit = findViewById(R.id.updateCommunityRules);
        showData(communityId);

        Button editCommunity = findViewById(R.id.editCommunityButton);
        editCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCommunity(communityId);
            }
        });
    }

    private void updateCommunity(String communityId) {
        if(TextUtils.isEmpty(commNameText)){
            Toast.makeText(UpdateCommunity.this, "Please enter community name", Toast.LENGTH_LONG).show();
            communityNameEdit.setError("Name is required");
            communityNameEdit.requestFocus();
        }
        else if (TextUtils.isEmpty(commDescText)){
            Toast.makeText(UpdateCommunity.this, "Please Enter community description", Toast.LENGTH_LONG).show();
            communityDescEdit.setError("Description is required");
            communityDescEdit.requestFocus();
        }
        else if(TextUtils.isEmpty(rulesText)) {
            Toast.makeText(UpdateCommunity.this, "Please Enter rules of community", Toast.LENGTH_LONG).show();
            rulesEdit.setError("Rules are required");
            rulesEdit.requestFocus();

        }else {
            commNameText = communityNameEdit.getText().toString();
            commDescText = communityDescEdit.getText().toString();
            rulesText = rulesEdit.getText().toString();

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference reference = database.getReference("Communities");
            DatabaseReference rules = database.getReference("Rules");
            Map<String, Object> map = new HashMap<>();
            map.put("description", commDescText);
            map.put("name", commNameText);

            reference.child(communityId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(UpdateCommunity.this, "Success!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateCommunity.this, "Error", Toast.LENGTH_LONG).show();
                }
            });
            Map<String, Object> ruleMap = new HashMap<>();
            map.put("description", rulesText);
            rules.child(communityId).updateChildren(ruleMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                }
            });
        }
    }
    private void showData(String communityId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Communities").child(communityId);
        DatabaseReference referenceRules = database.getReference("Rules").child(communityId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Community editCommunity = snapshot.getValue(Community.class);
                commNameText = editCommunity.getName();
                commDescText = editCommunity.getDescription();

                communityNameEdit.setText(commNameText);
                communityDescEdit.setText(commDescText);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateCommunity.this,"something wrong happened", Toast.LENGTH_LONG).show();

            }
        });
        referenceRules.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Rules editRules = snapshot.getValue(Rules.class);
                rulesText = editRules.getDescription();
                rulesEdit.setText(rulesText);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateCommunity.this,"something wrong happened", Toast.LENGTH_LONG).show();
            }
        });
    }
}