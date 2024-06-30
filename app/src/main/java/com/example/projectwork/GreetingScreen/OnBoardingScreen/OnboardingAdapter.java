package com.example.projectwork.GreetingScreen.OnBoardingScreen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.projectwork.GreetingScreen.OnBoardingScreen.FragmetnsBoarding.OnBoardingFragment1;
import com.example.projectwork.GreetingScreen.OnBoardingScreen.FragmetnsBoarding.OnBoardingFragment2;
import com.example.projectwork.GreetingScreen.OnBoardingScreen.FragmetnsBoarding.OnBoardingFragment3;

public class OnboardingAdapter extends FragmentPagerAdapter {

    public OnboardingAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OnBoardingFragment1();
            case 1:
                return new OnBoardingFragment2();
            case 2:
                return new OnBoardingFragment3();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3; // Количество фрагментов
    }
}
