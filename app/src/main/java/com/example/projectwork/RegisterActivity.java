package com.example.projectwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectwork.R;
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
    Spinner spinnerCountries;
    Button register;
    TextView login;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressBar progressBar;
    String selectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailRegister = findViewById(R.id.emailRegister);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        spinnerCountries = findViewById(R.id.spinner_countries);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        login.setOnClickListener(v -> {
            finish();
        });

        // Настройка Spinner
        String[] countries = {"USA", "Canada", "Mexico", "Germany", "France", "Italy", "Spain"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountries.setAdapter(adapter);

        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCountry = null;
            }
        });

        register.setOnClickListener(v -> {
            String emailText = emailRegister.getText().toString();
            String passwordText = password.getText().toString();
            String nameText = name.getText().toString();
            if (emailText.isEmpty() || passwordText.isEmpty() || nameText.isEmpty() || selectedCountry == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "Name, email, password and country are required", Toast.LENGTH_SHORT);
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
                            data.put("country", selectedCountry);
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
