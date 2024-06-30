package com.example.projectwork.GreetingScreen.OnBoardingScreen.FragmetnsBoarding;

import static android.content.Context.MODE_PRIVATE;
import static com.example.projectwork.LoginActivity.FIRST_TIME_KEY;
import static com.example.projectwork.LoginActivity.PREFS_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projectwork.LoginActivity;
import com.example.projectwork.MainActivity;
import com.example.projectwork.R;

public class OnBoardingFragment2 extends Fragment {

    private Button backButton;
    private Button nextButton;
    private ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_boarding2, container, false);

        backButton = view.findViewById(R.id.backButton);
        nextButton = view.findViewById(R.id.nextButton);
        viewPager = getActivity().findViewById(R.id.viewPager);

        backButton.setOnClickListener(v -> {
            // Ensure navigation logic does not interfere with onboarding flow
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        });

        nextButton.setOnClickListener(v -> {
            // Switch to the next fragment
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        });

        return view;
    }
}
