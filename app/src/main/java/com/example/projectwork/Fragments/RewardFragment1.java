package com.example.projectwork.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectwork.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RewardFragment1 extends Fragment {

    FirebaseAuth auth;
    FirebaseFirestore db;
    Button buttonBuy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reward1, container, false);

        buttonBuy = view.findViewById(R.id.buttonBuy);

        buttonBuy.setOnClickListener(v -> buyItem());

        return view;
    }

    private void buyItem() {
        String userId = auth.getCurrentUser().getUid();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Object pointsObject = documentSnapshot.get("points");

                long points = 0;
                if (pointsObject instanceof Number) {
                    points = ((Number) pointsObject).longValue();
                } else if (pointsObject instanceof String) {
                    try {
                        points = Long.parseLong((String) pointsObject);
                    } catch (NumberFormatException e) {
                        // Handle the case where the string is not a valid number
                    }
                }

                if (points >= 10) { // Assuming the item costs 10 points
                    userRef.update("points", points - 10).addOnSuccessListener(aVoid -> {
                        // Navigate back to ShopFragment
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentContainerView, new ShopFragment());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    });
                } else {
                    // Handle not enough points
                }
            }
        }).addOnFailureListener(e -> {
            // Handle error
        });
    }
}
