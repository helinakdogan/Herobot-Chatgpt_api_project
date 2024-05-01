package com.nexis.herobot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize UI elements
        editTextUsername = findViewById(R.id.signupUsername);
        editTextPassword = findViewById(R.id.signupPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        // Set a click listener for the sign up button
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve entered username and password
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Implement sign up logic here
                // For demonstration purposes, assume sign up is successful
                // In a real application, you would validate username and password and save them to your database

                // Successful sign up
                Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                // Redirect to LoginActivity
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                // Finish this activity to prevent going back to it when pressing back button
                finish();
            }
        });
    }
}
