package com.example.projectwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.projectwork.Fragments.HomeFragment;
import com.example.projectwork.Fragments.ProfileFragment;
import com.example.projectwork.Fragments.ShopFragment;
import com.example.projectwork.Fragments.TasksFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

//    TextView textView;
//    FirebaseFirestore db;
//    FirebaseAuth auth;
    BottomNavigationView bottomNavigationView;
    FrameLayout fragmentContainerView;

    private boolean locationPermissionGrant = false;

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
                case R.id.tasks:
                    fragment = new TasksFragment();
                    break;
                case R.id.shop:
                    fragment = new ShopFragment();
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