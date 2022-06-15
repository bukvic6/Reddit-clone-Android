package com.example.redditadroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.redditadroid.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddActivity extends AppCompatActivity {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference  = db.getReference("Posts");
    private EditText titleF,textF;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        titleF = findViewById(R.id.titlePost);
        textF = findViewById(R.id.textPost);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String user = currentUser.getUid();

        Button btnAdd = findViewById(R.id.btnAdd);



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String title = titleF.getText().toString();
                String text = textF.getText().toString();
                if (title.isEmpty() || text.isEmpty()) {
                    Toast.makeText(AddActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                    return;
                }
                if(title.length() < 3){
                    Toast.makeText(AddActivity.this, "Title must be minimum 8 characters", Toast.LENGTH_LONG).show();
                    return;
                }
                if(text.length() < 3){
                    Toast.makeText(AddActivity.this, "Title must be minimum 8 characters", Toast.LENGTH_LONG).show();
                    return;
                }
                Post post = new Post(title,text,user);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child(post.getText()).setValue(post);
                        Toast.makeText(AddActivity.this, "Post added..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddActivity.this,MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddActivity.this, "Fail to add Course..", Toast.LENGTH_SHORT).show();

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

    private void switchToHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

}
