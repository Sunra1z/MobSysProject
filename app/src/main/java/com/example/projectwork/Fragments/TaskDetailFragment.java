package com.example.projectwork.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectwork.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class TaskDetailFragment extends Fragment {

    TextView titleTask, descTask;
    Button priceButton;
    ImageView imageTask;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userPointsRef = database.getReference("userPoints");

    private String desc, title, imageUrl, price;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);

        titleTask = view.findViewById(R.id.titleTaskDetailTextView);
        descTask = view.findViewById(R.id.textViewDescTask);
        imageTask = view.findViewById(R.id.taskDetailImageView);
        priceButton = view.findViewById(R.id.taskPriceDetail);

        if (getArguments() != null) {
            title = getArguments().getString("title");
            desc = getArguments().getString("desc");
            imageUrl = getArguments().getString("image");
            price = getArguments().getString("price");
        }

        titleTask.setText(title);
        descTask.setText(desc);
        priceButton.setText(price);

        Glide.with(this)
                .load(imageUrl)
                .into(imageTask);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // get the current user's id
        DocumentReference userPointsRef = db.collection("users").document(userId);

        priceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the current user points from Firestore
                userPointsRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String currentUserPoints = documentSnapshot.getString("points");

                        // Check if the value is not null
                        if (currentUserPoints != null) {
                            // Add the price of the task to the user's points
                            Long updatedPoints = Long.parseLong(currentUserPoints) + Long.parseLong(price);
                            // Update the user's points in Firestore
                            userPointsRef.update("points", updatedPoints.toString());
                            TasksFragment tasksFragment = new TasksFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragmentContainerView, tasksFragment)
                                    .addToBackStack(null)
                                    .commit();

                        } else {
                            // Handle the case where the userPoints node does not exist or does not contain a value
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle possible errors.
                    }
                });
            }
        });


        return view;
    }

}