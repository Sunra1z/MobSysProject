package com.example.projectwork.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.projectwork.CompasActivity;
import com.example.projectwork.LoginActivity;
import com.example.projectwork.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    FirebaseFirestore db;
    Button logout;
    FirebaseAuth auth;
    TextView txtName, txtCountry, pointsProfile;
    ImageView imgProfile, compassIcon;

    ProgressBar progressBar;
    RelativeLayout pointCard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        logout = view.findViewById(R.id.logout);
        txtName = view.findViewById(R.id.txtName);
        txtCountry = view.findViewById(R.id.txtCountry);
        progressBar = view.findViewById(R.id.progressBar);
        imgProfile = view.findViewById(R.id.profile_pic);
        compassIcon = view.findViewById(R.id.header_compass);
        pointsProfile = view.findViewById(R.id.PointsScoreProfile);
        pointCard = view.findViewById(R.id.PointsView);

        //logout button
        logout.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Showing the progress bar to avoid showing unloaded data
        progressBar.setVisibility(View.VISIBLE);

        // Get user data
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                txtName.setText(documentSnapshot.getString("name"));
                txtCountry.setText(documentSnapshot.getString("country"));
                pointsProfile.setText(documentSnapshot.getString("points"));
                String profilePicUrl = documentSnapshot.getString("profilePic");

                if (isAdded()){
                    // setting IMG profile from FireBase URL using Glide library
                    Glide.with(this)
                            .load(profilePicUrl)
                            .circleCrop()
                            .into(imgProfile);
                }
            } else {
                // Handle the case where the document does not exist
                txtName.setText("");
                txtCountry.setText("");
                pointsProfile.setText("0");
            }
            // when user found loading animation is gone
            // showing user data
            progressBar.setVisibility(View.GONE);
            imgProfile.setVisibility(View.VISIBLE);
            txtName.setVisibility(View.VISIBLE);
            txtCountry.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            pointCard.setVisibility(View.VISIBLE);


        }).addOnFailureListener(e -> {
            // Handle the error
            txtName.setText("");
            txtCountry.setText("");
            pointsProfile.setText("");
            progressBar.setVisibility(View.GONE);
        });
        compassIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CompasActivity.class);
                startActivity(intent);
            }
        });
    }
}
