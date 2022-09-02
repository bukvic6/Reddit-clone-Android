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

import com.example.redditadroid.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {

    private EditText usernameEdit, emailEdit ,oldPassword, newPassword;
    private String textUsername, textEmail, truOldPass,textOldPassword,textNewPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getSupportActionBar().setTitle("Update Profile Details");

        usernameEdit = findViewById(R.id.editText_updateProfile_username);
        emailEdit = findViewById(R.id.editText_updateProfile_email);
        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        showProfile(currentUser);
        Button buttonUploadProfile = findViewById(R.id.button_update_profile);
        buttonUploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(currentUser);

            }
        });
    }
    private void updateProfile(FirebaseUser currentUser) {
        if(TextUtils.isEmpty(textUsername)){
            Toast.makeText(UpdateProfile.this, "Please Enter Username", Toast.LENGTH_LONG).show();
            usernameEdit.setError("Username is required");
            usernameEdit.requestFocus();
        }
        else if (TextUtils.isEmpty(textEmail)){
            Toast.makeText(UpdateProfile.this, "Please Enter email", Toast.LENGTH_LONG).show();
            emailEdit.setError("Email is required");
            emailEdit.requestFocus();
        } else{
            textUsername = usernameEdit.getText().toString();
            textEmail = emailEdit.getText().toString();
            textOldPassword = oldPassword.getText().toString();
            textNewPass = newPassword.getText().toString();


            if (textOldPassword.equals(truOldPass)){
                Toast.makeText(UpdateProfile.this, "Password updated", Toast.LENGTH_LONG).show();
                truOldPass = textNewPass;
            } else {
                Toast.makeText(UpdateProfile.this, "", Toast.LENGTH_LONG).show();
            }
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference reference = database.getReference("users").child(currentUser.getUid());

            User write = new User(textUsername,textEmail,truOldPass);
            reference.setValue(write).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().
                                setDisplayName(textUsername).build();
                        currentUser.updateProfile(profileUpdates);

                        Toast.makeText(UpdateProfile.this, "Update successful", Toast.LENGTH_LONG).show();
                        Intent intent  = new Intent(UpdateProfile.this, ProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } else{
                        try{
                            throw task.getException();
                        } catch (Exception e){
                            Toast.makeText(UpdateProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }
    private void showProfile(FirebaseUser currentUser){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                textUsername = userProfile.username;
                textEmail = userProfile.email;
                truOldPass = userProfile.password;


                emailEdit.setText(textEmail);
                usernameEdit.setText(textUsername);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfile.this,"something wrong happened", Toast.LENGTH_LONG).show();


            }
        });

    }
}