package com.enochtam.cisc325.makefit.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enochtam.cisc325.makefit.R;

import butterknife.ButterKnife;

public class WorkoutList extends Fragment {

    View fragmentView;
    Activity that;

    public WorkoutList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_workout_list, container, false);
        ButterKnife.bind(this,fragmentView);

        return fragmentView;
    }


}

