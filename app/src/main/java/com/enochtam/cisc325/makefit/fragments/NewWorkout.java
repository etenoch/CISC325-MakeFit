package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewWorkout extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    View fragmentView;
    MainActivity that;

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

    @Override public void onStart() {
        super.onStart();
        that.setToolbarTitle("New Workout");
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

    @Override public void onDetach() {
        super.onStart();
        if (that.actionBar != null) {
            that.actionBar.setDisplayHomeAsUpEnabled(false);
            that.actionBar.setHomeButtonEnabled(false);
        }
        that.drawerToggle.setDrawerIndicatorEnabled(true);
        that.drawerToggle.setToolbarNavigationClickListener(null);
        that.drawerToggle.syncState();
        that.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
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
        fragmentView= inflater.inflate(R.layout.fragment_new_workout, container, false);
        ButterKnife.bind(this, fragmentView);
        that.setupCloseKeyboard(fragmentView);

        spinnerAdapter = ArrayAdapter.createFromResource(that, R.array.difficulty_spinner, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        difficultySpinner.setAdapter(spinnerAdapter);

        return fragmentView;
    }


}


