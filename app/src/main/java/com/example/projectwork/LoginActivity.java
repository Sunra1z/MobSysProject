package com.example.projectwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    TextView register;
    Button login;
    TextView errorTextView;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        errorTextView = findViewById(R.id.error);

        register.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        login.setOnClickListener(v -> {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            if (emailText.isEmpty() || passwordText.isEmpty()) {
                displayError("Email and password are required");
                return;
            }

            auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            displayError("Invalid email or password");
                        }
                    });
        });
    }

    //stay logged in
    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }



    private void displayError(String message) {
        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
    }
}
