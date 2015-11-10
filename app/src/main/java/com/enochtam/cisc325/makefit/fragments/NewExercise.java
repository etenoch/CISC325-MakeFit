package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewExercise extends Fragment {

    String prevFragmentTitle;

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    View fragmentView;
    MainActivity that;

    @Bind(R.id.exercise_name) EditText exerciseName;
    @Bind(R.id.instructions) EditText instructions;

    public static WorkoutScreen newInstance(String param1, String param2) {
        WorkoutScreen fragment = new WorkoutScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public NewExercise() {
        // Required empty public constructor
    }

    @Override public void onStart() {
        super.onStart();
        prevFragmentTitle = that.getToolbarTitle();
        that.setToolbarTitle("New Exercise");
    }
    @Override public void onDetach() {
        that.setToolbarTitle(prevFragmentTitle);
        super.onDetach();
    }

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        that = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_new_exercise, container, false);
        ButterKnife.bind(this,fragmentView);

        return fragmentView;
    }


}

