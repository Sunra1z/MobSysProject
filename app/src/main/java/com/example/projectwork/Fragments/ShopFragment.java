package com.example.projectwork.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectwork.Fragments.RewardFragment1;
import com.example.projectwork.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShopFragment extends Fragment {
    FirebaseFirestore db;
    FirebaseAuth auth;
    TextView pointsProfile;
    CardView cardView1;

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
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        pointsProfile = view.findViewById(R.id.PointsScoreProfile);
        cardView1 = view.findViewById(R.id.cardView1);

        cardView1.setOnClickListener(v -> {
            // Navigate to the RewardFragment1
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainerView, new RewardFragment1());
            transaction.addToBackStack(null);
            transaction.commit();

        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get user data
        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                pointsProfile.setText(documentSnapshot.getString("points"));
            } else {
                // Handle the case where the document does not exist
                pointsProfile.setText("0");
            }
        }).addOnFailureListener(e -> {
            // Handle the error
            pointsProfile.setText("");
        });
    }
}
