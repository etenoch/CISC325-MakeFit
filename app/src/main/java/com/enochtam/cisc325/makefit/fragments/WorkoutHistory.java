package com.enochtam.cisc325.makefit.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;


public class WorkoutHistory extends Fragment {

    public Fragment thisInstance;
    public View fragmentView;
    MainActivity that;


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = (MainActivity) getActivity();
    }

    @Override public void onResume() {
        super.onStart();
        that.setToolbarTitle("Workout History");
    }


    public WorkoutHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_workout_history, container, false);
        return fragmentView;
    }


}
