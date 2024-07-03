package com.example.projectwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.projectwork.Fragments.HomeFragment;
import com.example.projectwork.Fragments.ProfileFragment;
import com.example.projectwork.Fragments.Test1Fragment;
import com.example.projectwork.Fragments.Test2Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

//    TextView textView;
//    FirebaseFirestore db;
//    FirebaseAuth auth;
    BottomNavigationView bottomNavigationView;
    FrameLayout fragmentContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentContainerView = findViewById(R.id.fragmentContainerView);


        //set default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new HomeFragment()).commit();

        //set bottom navigation without swe
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    fragment = new HomeFragment();
                    break;
                case R.id.test1:
                    fragment = new Test1Fragment();
                    break;
                case R.id.test2:
                    fragment = new Test2Fragment();
                    break;
                case R.id.profile:
                    fragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
            return true;
        });




//        textView = findViewById(R.id.textView);
//        db = FirebaseFirestore.getInstance();
//        auth = FirebaseAuth.getInstance();
//
//      //show usersname's email
//
//        db.collection("users")
//                .whereEqualTo("email", auth.getCurrentUser().getEmail())
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            textView.setText(document.getString("name"));
//                        }
//                    }
//                });

    }
}