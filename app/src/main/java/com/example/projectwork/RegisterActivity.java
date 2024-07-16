package com.example.projectwork;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectwork.DataClasses.UtilClass;
import com.example.projectwork.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class RegisterActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> imagePickLauncher;


    EditText emailRegister;
    EditText password;
    EditText name;
    ImageView imageChooser;
    Spinner spinnerCountries;
    Button register;
    TextView login;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressBar progressBar;
    String selectedCountry;

    Uri selectedImageUri;

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
        imageChooser = findViewById(R.id.profile_pic_add);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        login.setOnClickListener(v -> {
            finish();
        });

        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null && data.getData()!=null){
                            selectedImageUri = data.getData();
                            UtilClass.setProfilePic(getBaseContext(), selectedImageUri, imageChooser);
                        }
                    }
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

        imageChooser.setOnClickListener(v -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
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
                            UtilClass.uploadProfilePic(selectedImageUri, user.getUid())
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Uri downloadUri = task1.getResult();
                                            data.put("profilePic", downloadUri.toString());
                                            db.collection("users")
                                                    .document(user.getUid())
                                                    .set(data)
                                                    .addOnCompleteListener(task2 -> {
                                                        progressBar.setVisibility(View.GONE);
                                                        if (task2.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Failed to register user", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "Failed to upload picture", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Failed to register user", Toast.LENGTH_SHORT).show();
                        }
                    });
            });
    }
}