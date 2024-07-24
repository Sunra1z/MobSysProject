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
                String pointsObject = documentSnapshot.getString("points");
                long points = 0;
                if (pointsObject != null) {
                    try {
                        points = Long.parseLong(pointsObject);
                    } catch (NumberFormatException e) {
                        // Handle the case where the string is not a valid number
                    }
                }
                // TODO: newPoints - actual price of an item
                if (points >= 10) { // Assuming the item costs 10 points
                    long newPoints = points - 10;
                    userRef.update("points", String.valueOf(newPoints)).addOnSuccessListener(aVoid -> {
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
