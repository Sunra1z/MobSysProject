package com.example.projectwork.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectwork.DataClasses.WasteDisposalItem;
import com.example.projectwork.R;

import java.util.List;

public class homeFragmentRecyclerAdapter extends RecyclerView.Adapter<homeFragmentRecyclerAdapter.ViewHolder> {

    private List<WasteDisposalItem> itemList;

    public homeFragmentRecyclerAdapter(List<WasteDisposalItem> itemList) {
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemText;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemText = itemView.findViewById(R.id.item_text);
        }
    }


    //test

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_waste_disposal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WasteDisposalItem item = itemList.get(position);
        holder.itemImage.setImageResource(item.getImageResId());
        holder.itemText.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

