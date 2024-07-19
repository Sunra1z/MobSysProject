package com.example.projectwork.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectwork.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {
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


    }

}
