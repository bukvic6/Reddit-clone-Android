package com.example.redditadroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.redditadroid.model.Community;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCommunityActivity extends AppCompatActivity {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = db.getReference("Communities");
    private EditText nameF, descriptionF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_community);
        nameF = findViewById(R.id.communityName);
        descriptionF = findViewById(R.id.communityDescription);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String user = currentUser.getUid();
        Button btnCreate = findViewById(R.id.btnCreateComm);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameF.getText().toString();
                String description = descriptionF.getText().toString();
                if (name.isEmpty() || description.isEmpty()) {
                    Toast.makeText(AddCommunityActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                    return;
                }
                if (name.length() < 3) {
                    Toast.makeText(AddCommunityActivity.this, "Name must be minimum 8 characters", Toast.LENGTH_LONG).show();
                    return;
                }
                if (description.length() < 3) {
                    Toast.makeText(AddCommunityActivity.this, "Description must be minimum 8 characters", Toast.LENGTH_LONG).show();
                    return;
                }
                Community community = new Community(name, description, user);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child(community.getName()).setValue(community);
                        Toast.makeText(AddCommunityActivity.this, "Community created successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddCommunityActivity.this, MainActivity.class));


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddCommunityActivity.this, "Fail to add Community..", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });
        Button btnBa = findViewById(R.id.btnBack);
        btnBa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToHome();
            }
        });
    }

    private void switchToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
