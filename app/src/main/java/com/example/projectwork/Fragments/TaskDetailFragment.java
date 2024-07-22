package com.example.projectwork.Fragments;

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



public class TaskDetailFragment extends Fragment {

    TextView titleTask, descTask;
    Button priceButton;
    ImageView imageTask;

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


        return view;
    }
}
