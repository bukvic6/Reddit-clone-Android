package com.example.redditadroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redditadroid.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_Form extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            finish();
            return;
        }
        Button btnRegister = findViewById(R.id.signupbutton);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();

            }
        });
        TextView textViewToLogin = findViewById(R.id.switchToLogin);
        textViewToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToLogin();
            }
        });
    }
    private void registerUser() {
        EditText usernameF = findViewById(R.id.registerUsername);
        EditText emailF = findViewById(R.id.registerEmail);
        EditText passwordF = findViewById(R.id.registerPassword);

        String username = usernameF.getText().toString();
        String email = emailF.getText().toString();
        String password = passwordF.getText().toString();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length() < 8){
            Toast.makeText(this, "Password must be minimum 8 characters", Toast.LENGTH_LONG).show();
            return;
        }
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String displayName = "";
                    String description = "";
                    User user = new User(username,email,password,displayName,description);
                    FirebaseDatabase.getInstance("https://redditadroid-default-rtdb.firebaseio.com/").getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>(){
                                @Override
                        public void onComplete(@NonNull Task<Void> task){
                          showMainActivity();
                    }});
                } else {
                    Toast.makeText(Signup_Form.this, "Authentication failed.",
                            Toast.LENGTH_LONG).show();
                }
            }});
    }
    private void showMainActivity(){
      Intent intent = new Intent(this, MainActivity.class);
      startActivity(intent);
      finish();
    }
    private void switchToLogin(){
        Intent intent = new Intent(this, Login_form.class);
        startActivity(intent);
        finish();

    }

}