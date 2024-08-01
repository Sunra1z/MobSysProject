package com.example.projectwork.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectwork.DataClasses.TaskDataClass;
import com.example.projectwork.Fragments.TaskDetailFragment;
import com.example.projectwork.R;

import java.util.ArrayList;
import java.util.List;


public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter.TaskViewHolder> {

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


    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView taskImageView;
        TextView priceTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.task_title_text_view);
            descriptionTextView = itemView.findViewById(R.id.task_description_text_view);
            taskImageView = itemView.findViewById(R.id.task_image_view);
            priceTextView = itemView.findViewById(R.id.task_price_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        TaskDataClass taskDataClass = tasks.get(position);

                        TaskDetailFragment taskDetailFragment = new TaskDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("title", taskDataClass.getTaskTitle());
                        bundle.putString("desc", taskDataClass.getTaskDesc());
                        bundle.putString("image", taskDataClass.getTaskImage());
                        bundle.putString("price", taskDataClass.getTaskScore());
                        taskDetailFragment.setArguments(bundle);

                        FragmentManager fragmentManager = ((AppCompatActivity)v.getContext()).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, taskDetailFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });



        }

    }
    
}


