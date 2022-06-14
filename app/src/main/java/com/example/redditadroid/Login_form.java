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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_form extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            finish();
            return;
        }
        Button btnLogin = findViewById(R.id.containedButton);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUser();
            }
        });
        TextView switchToRegister =  findViewById(R.id.switchToRegister);
        switchToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToRegisterForm();
            }
        });
    }
    private void authenticateUser(){
        EditText emailF = findViewById(R.id.logEmail);
        EditText passwordF = findViewById(R.id.logPassword);

        String email = emailF.getText().toString();
        String password = passwordF.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            showMainForm();
                        } else {
                            Toast.makeText(Login_form.this, "Email or pass incorect",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
        });
    }
    private void showMainForm(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void switchToRegisterForm(){
        Intent intent = new Intent(this, Signup_Form.class);
        startActivity(intent);
        finish();
    }

}