package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.models.Workout;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WorkoutDetails extends Fragment {

    public Workout workout;

    public View fragmentView;

    @Bind(R.id.workout_name) TextView workoutName;

    public WorkoutDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_workout_details, container, false);
        ButterKnife.bind(this, fragmentView);


        return fragmentView;
    }


    public void setWorkout(Workout workout){
        this.workout = workout;
    }

    public void populateViews(){
        if(workout!=null){
            workoutName.setText(workout.name);
        }
    }

}
