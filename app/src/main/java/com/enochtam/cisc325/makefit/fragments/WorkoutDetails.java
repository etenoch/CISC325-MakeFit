package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.models.Workout;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WorkoutDetails extends Fragment {

    public Workout workout;

    public View fragmentView;
    MainActivity that;

    String prevFragmentTitle;


    @Bind(R.id.workout_name) TextView workoutName;
    @Bind(R.id.workout_difficulty) TextView workoutDifficulty;
    @Bind(R.id.workout_details) TextView workoutDetails;
    @Bind(R.id.workout_estimated_time) TextView estimatedTime;
    @Bind(R.id.start_workout_button) Button startButton;

    public boolean changeToolbar = false; // true if NOT on tablet/embedded

    public WorkoutDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_workout_details, container, false);
        ButterKnife.bind(this, fragmentView);


        return fragmentView;
    }


    @Override public void onStart() {
        super.onStart();
        if(changeToolbar){
            prevFragmentTitle = that.getToolbarTitle();
            that.setToolbarTitle("Workout Details");

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


    public void setWorkout(Workout workout){
        this.workout = workout;
    }

    public void populateViews(){
        if(workout!=null){
            workoutName.setText(workout.name);
            workoutDifficulty.setText(workout.difficulty);
            workoutDetails.setText(workout.details);
            estimatedTime.setText("Estimated Time: " + Integer.toString(workout.getTotalTime()) + " minutes");
//            startButton
        }
    }

}
