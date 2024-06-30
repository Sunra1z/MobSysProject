package com.example.projectwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText emailRegister;
    EditText password;
    EditText name;
    Button register;
    TextView login;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailRegister = findViewById(R.id.emailRegister);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        login.setOnClickListener(v -> {
            finish();
        });


        register.setOnClickListener(v -> {
            String emailText = emailRegister.getText().toString();
            String passwordText = password.getText().toString();
            String nameText = name.getText().toString();
            if (emailText.isEmpty() || passwordText.isEmpty() || nameText.isEmpty()) {
                Toast toast = Toast.makeText(getApplicationContext(), "Name, email and password are required", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            Map<String, Object> data = new HashMap<>();
                            data.put("name", nameText);
                            data.put("email", emailText);
                            data.put("password", passwordText);
                            db.collection("users")
                                    .document(user.getUid())
                                    .set(data)
                                    .addOnCompleteListener(task1 -> {
                                        progressBar.setVisibility(View.GONE);
                                        if (task1.isSuccessful()) {
                                            Toast toast = Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT);
                                            toast.show();
                                            finish();
                                        } else {
                                            Toast toast = Toast.makeText(getApplicationContext(), "Failed to register user", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast toast = Toast.makeText(getApplicationContext(), "Failed to register user", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
        });









    }
}
