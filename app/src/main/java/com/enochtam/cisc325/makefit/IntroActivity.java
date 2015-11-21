package com.enochtam.cisc325.makefit;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {

        showSkipButton(false);
        showDoneButton(false);

        addSlide(AppIntroFragment.newInstance(
                "Welcome to MakeFit",
                "A revolutionary new fitness app that allows you to lead a healthier lifestyle.", R.drawable.ic_icon_vector_white,
                getResources().getColor(R.color.primary)
        ));

        addSlide(AppIntroFragment.newInstance(
                "Quick and simple to get started",
                "Use our exercises to guide your workouts", R.drawable.ic_weights,
                getResources().getColor(R.color.accent_green)
        ));

        addSlide(AppIntroFragment.newInstance(
                "Customize your workouts",
                "Create your own custom exercises and workouts", R.drawable.ic_pullup,
                Color.parseColor("#3F51B5")
        ));

        addSlide(AppIntroFragment.newInstance(
                "Track your progress",
                "View your exercise history conveniently", R.drawable.ic_calendar,
                Color.parseColor("#009688")
        ));

        showDoneButton(true);

        setDoneText("Get Started");

    }


    @Override public void onSkipPressed() {

    }

    @Override public void onDonePressed() {
        Intent setupProfile = new Intent(this,SetupProfileActivity.class);
        startActivity(setupProfile);
    }
}