package com.example.projectwork.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectwork.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    FirebaseFirestore db;
    Button logout;
    FirebaseAuth auth;
    TextView txtName, txtEmail, txtCountry;
    ImageView imgProfile;

    ProgressBar progressBar;

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
        txtEmail = view.findViewById(R.id.txtemail);
        txtCountry = view.findViewById(R.id.txtCountry);
        progressBar = view.findViewById(R.id.progressBar);
        imgProfile = view.findViewById(R.id.profile_pic);

        //logout button
        logout.setOnClickListener(v -> {
            auth.signOut();
            getActivity().finish();
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
                txtEmail.setText(documentSnapshot.getString("email"));
                txtCountry.setText(documentSnapshot.getString("country"));
            } else {
                // Handle the case where the document does not exist
                txtName.setText("");
                txtEmail.setText("");
                txtCountry.setText("");
            }
            // when user found loading animation is gone
            // showing user data
            progressBar.setVisibility(View.GONE);
            imgProfile.setVisibility(View.VISIBLE);
            txtName.setVisibility(View.VISIBLE);
            txtEmail.setVisibility(View.VISIBLE);
            txtCountry.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);


        }).addOnFailureListener(e -> {
            // Handle the error
            txtName.setText("");
            txtEmail.setText("");
            txtCountry.setText("");
            progressBar.setVisibility(View.GONE);
        });
    }
}
