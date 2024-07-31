package com.example.projectwork.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectwork.Adapters.homeFragmentRecyclerAdapter;
import com.example.projectwork.CompasActivity;
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
        ImageView imageView = view.findViewById(R.id.imageView7);

        List<WasteDisposalItem> items = new ArrayList<>();
        items.add(new WasteDisposalItem(R.drawable.img_1, "Paper"));
        items.add(new WasteDisposalItem(R.drawable.img_2, "Metal"));
        items.add(new WasteDisposalItem(R.drawable.img_3, "Plastic"));
        items.add(new WasteDisposalItem(R.drawable.img_4, "Glass"));


        homeFragmentRecyclerAdapter adapter = new homeFragmentRecyclerAdapter(items, new homeFragmentRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(WasteDisposalItem item) { // This is bullshit but it works
                Fragment selectedFragment = null;
                if (item.getTitle().equals("Plastic")){
                    selectedFragment = new PlasticFragment();
                } else if (item.getTitle().equals("Glass")) {
                    selectedFragment = new GlassFragment();
                } else if (item.getTitle().equals("Paper")){
                    selectedFragment = new PaperFragment();
                } else if (item.getTitle().equals("Metal")) {
                    selectedFragment = new MetalFragment();
                }
                if (selectedFragment != null){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, selectedFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
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

        imageView.setOnClickListener(v -> {
           Intent intent = new Intent(getActivity(), CompasActivity.class);
              startActivity(intent);
        });

        return view;
    }
}
