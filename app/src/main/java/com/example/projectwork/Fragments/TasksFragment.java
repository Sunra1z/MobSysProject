package com.example.projectwork.Fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectwork.Adapters.TasksRecyclerAdapter;
import com.example.projectwork.DataClasses.TaskDataClass;
import com.example.projectwork.R;
import com.example.projectwork.TaskUploadActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {

    FloatingActionButton createTask;
    RecyclerView recyclerView;
    List<TaskDataClass> tasksList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        createTask = view.findViewById(R.id.upload_task_button);
        recyclerView = view.findViewById(R.id.tasks_recyclerView);
        progressBar = view.findViewById(R.id.progress_bar_tasks);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setVisibility(View.INVISIBLE);

        tasksList = new ArrayList<>();

        TasksRecyclerAdapter adapter = new TasksRecyclerAdapter(getContext(), tasksList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");


        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasksList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()){
                    for (DataSnapshot taskSnapshot : userSnapshot.getChildren()){
                        TaskDataClass taskDataClass = taskSnapshot.getValue(TaskDataClass.class);
                        tasksList.add(taskDataClass);
                    }
                }
                adapter.notifyDataSetChanged();

                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

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