package com.example.projectwork.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectwork.R;
import com.example.projectwork.TaskUploadActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TasksFragment extends Fragment {

    FloatingActionButton createTask;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        createTask = view.findViewById(R.id.upload_task_button);

        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TaskUploadActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}