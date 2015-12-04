package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.models.Exercise;
import com.enochtam.cisc325.makefit.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ExerciseDetails extends Fragment {


    public View fragmentView;
    MainActivity that;

    String prevFragmentTitle;

    RecyclerView.LayoutManager exercisesLayoutManager;

    @Bind(R.id.exercise_name) TextView exerciseName;
    @Bind(R.id.exercise_details) TextView exerciseDetails;
    @Bind(R.id.exercise_time) TextView exerciseTime;
    @Bind(R.id.exercise_image) ImageView exerciseImage;


    Exercise exercise;


    public boolean changeToolbar = false; // true if NOT on tablet/embedded

    public ExerciseDetails() {
        // Required empty public constructor
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = (MainActivity) getActivity();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_exercise_details, container, false);
        ButterKnife.bind(this, fragmentView);


        return fragmentView;
    }


    @Override public void onStart() {
        super.onStart();
        if(changeToolbar){
            prevFragmentTitle = that.getToolbarTitle();
            that.setToolbarTitle("Exercise Details");

            that.drawerToggle.setDrawerIndicatorEnabled(false);
            that.drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getFragmentManager().popBackStackImmediate();
                }
            });
            if (that.actionBar != null) {
                that.actionBar.setDisplayHomeAsUpEnabled(true);
                that.actionBar.setHomeButtonEnabled(true);
            }
            that.drawerToggle.syncState();
            that.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override public void onDetach() {
        if(changeToolbar) {
            if (that.actionBar != null) {
                that.actionBar.setDisplayHomeAsUpEnabled(false);
                that.actionBar.setHomeButtonEnabled(false);
            }
            that.drawerToggle.setDrawerIndicatorEnabled(true);
            that.drawerToggle.setToolbarNavigationClickListener(null);
            that.drawerToggle.syncState();
            that.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            that.setToolbarTitle(prevFragmentTitle);
        }
        super.onDetach();
    }


    public void setExercise(Exercise exercise){
        this.exercise = exercise;
    }

    public void populateViews(){
        if(exercise!=null){
            exerciseName.setText(exercise.name);
            exerciseDetails.setText(exercise.details);
            exerciseTime.setText( Utils.formatDuration(exercise.time) );

            if(exercise.imageUri!=null){
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        exerciseImage.setImageURI(exercise.imageUri);
                    }
                }, 200);
            }
        }
    }


}
