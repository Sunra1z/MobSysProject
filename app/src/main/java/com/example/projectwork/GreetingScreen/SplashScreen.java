package com.example.projectwork.GreetingScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.projectwork.GreetingScreen.OnBoardingScreen.OnboardingActivity;
import com.example.projectwork.LoginActivity;
import com.example.projectwork.R;

public class SplashScreen extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String FIRST_TIME_KEY = "first_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if onboarding has been completed
                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                boolean firstTime = preferences.getBoolean(FIRST_TIME_KEY, true);

                Intent intent;
                if (firstTime) {
                    intent = new Intent(SplashScreen.this, OnboardingActivity.class);
                } else {
                    intent = new Intent(SplashScreen.this, LoginActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
