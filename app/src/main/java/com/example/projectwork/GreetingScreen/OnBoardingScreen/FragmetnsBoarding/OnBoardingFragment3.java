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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projectwork.LoginActivity;
import com.example.projectwork.MainActivity;
import com.example.projectwork.R;

public class OnBoardingFragment3 extends Fragment {

    private Button backButton;
    private Button finishButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_boarding3, container, false);

        backButton = view.findViewById(R.id.backButton);
        finishButton = view.findViewById(R.id.finishButton);

        backButton.setOnClickListener(v -> {
            // Ensure navigation logic does not interfere with onboarding flow
            getActivity().onBackPressed();
        });

        finishButton.setOnClickListener(v -> {
            // Ensure onboarding is marked as complete and navigate to LoginActivity
            SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(FIRST_TIME_KEY, false);
            editor.apply();

            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        return view;
    }
}
