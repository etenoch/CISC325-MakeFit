package com.enochtam.cisc325.makefit.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.enochtam.cisc325.makefit.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewWorkout extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    View fragmentView;
    Activity that;

    @Bind(R.id.workout_difficulty) Spinner difficultySpinner;

    ArrayAdapter spinnerAdapter;

    public static WorkoutScreen newInstance(String param1, String param2) {
        WorkoutScreen fragment = new WorkoutScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public NewWorkout() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        that = getActivity();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView= inflater.inflate(R.layout.fragment_new_workout, container, false);
        ButterKnife.bind(this, fragmentView);


        spinnerAdapter = ArrayAdapter.createFromResource(that, R.array.difficulty_spinner, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        difficultySpinner.setAdapter(spinnerAdapter);


        return fragmentView;
    }


}


