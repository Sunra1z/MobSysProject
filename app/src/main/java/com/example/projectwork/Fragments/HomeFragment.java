package com.example.projectwork.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectwork.Adapters.homeFragmentRecyclerAdapter;
import com.example.projectwork.DataClasses.WasteDisposalItem;
import com.example.projectwork.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        ShapeableImageView newsButton = view.findViewById(R.id.imageView6);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<WasteDisposalItem> items = new ArrayList<>();
        items.add(new WasteDisposalItem(R.drawable.img_1, "Paper"));
        items.add(new WasteDisposalItem(R.drawable.img_2, "Metal"));
        items.add(new WasteDisposalItem(R.drawable.img_3, "Plastic"));
        items.add(new WasteDisposalItem(R.drawable.img_4, "Glass"));


        homeFragmentRecyclerAdapter adapter = new homeFragmentRecyclerAdapter(items);
        recyclerView.setAdapter(adapter);

        newsButton.setOnClickListener(v -> {
            // New instance for News Fragment
            NewsFragment newsFragment = new NewsFragment();

            // Using FragmentTransaction to replace fragment
            // not null safe but meh, it works :)
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, newsFragment)
                    .addToBackStack(null) // back stack
                    .commit();
        });

        return view;
    }
}
