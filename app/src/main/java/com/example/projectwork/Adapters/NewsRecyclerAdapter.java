package com.example.projectwork.Adapters;

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

import com.example.projectwork.Fragments.NewsDetailFragment;
import com.example.projectwork.R;
import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {

    public List<Article> articleList;

    // at least it works
    public NewsRecyclerAdapter(List<Article> articleList){
        this.articleList = articleList;
    }

    public void updateData(List<Article> data){
        articleList.clear();
        articleList.addAll(data);
    }


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_card, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.descriptionTextView.setText(article.getDescription());
        Picasso.get().load(article.getUrlToImage())
                .error(R.drawable.baseline_image_not_supported_24)
                .into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView, descriptionTextView;
        ImageView imageView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.article_title_text_view);
            descriptionTextView = itemView.findViewById(R.id.article_description_text_view);
            imageView = itemView.findViewById(R.id.article_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        Article article = articleList.get(position);

                        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("url", article.getUrl());
                        newsDetailFragment.setArguments(bundle);

                        FragmentManager fragmentManager = ((AppCompatActivity)v.getContext()).getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView, newsDetailFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });

        }
    }
}
