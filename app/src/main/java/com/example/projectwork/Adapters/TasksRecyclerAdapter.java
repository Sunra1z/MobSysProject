package com.example.projectwork.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectwork.DataClasses.TaskDataClass;
import com.example.projectwork.R;

import java.util.List;


public class TasksRecyclerAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private Context context;

    private List<TaskDataClass> tasks;

    public TasksRecyclerAdapter(Context context, List <TaskDataClass> tasks){
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tasks_card, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskDataClass task = tasks.get(position);

        // Set the data to view elements
        Glide.with(context).load(tasks.get(position).getTaskImage()).into(holder.taskImageView);
        holder.titleTextView.setText(tasks.get(position).getTaskTitle());
        holder.descriptionTextView.setText(tasks.get(position).getTaskDesc());
        holder.priceTextView.setText(tasks.get(position).getTaskScore());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}


