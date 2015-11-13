package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewWorkout extends Fragment {

    Fragment thisInstance;
    String prevFragmentTitle;

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    View fragmentView;
    MainActivity that;

    Menu fragmentMenu;

    @Bind(R.id.workout_difficulty) Spinner difficultySpinner;
    @Bind(R.id.pick_exercise_button) Button pickExerciseButton;
    @Bind(R.id.add_custom_button) Button addCustomButton;

    ArrayAdapter spinnerAdapter;

    public static WorkoutScreen newInstance(String param1, String param2) {
        WorkoutScreen fragment = new WorkoutScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public NewWorkout() {
        thisInstance = this;
    }

    @Override public void onStart() {
        super.onStart();
        prevFragmentTitle = that.getToolbarTitle();
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
        if (that.actionBar != null) {
            that.actionBar.setDisplayHomeAsUpEnabled(false);
            that.actionBar.setHomeButtonEnabled(false);
        }
        that.drawerToggle.setDrawerIndicatorEnabled(true);
        that.drawerToggle.setToolbarNavigationClickListener(null);
        that.drawerToggle.syncState();
        that.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        that.setToolbarTitle(prevFragmentTitle);
        super.onDetach();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        that = (MainActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView= inflater.inflate(R.layout.fragment_new_workout, container, false);
        ButterKnife.bind(this, fragmentView);
//        that.setupCloseKeyboard(fragmentView);

        spinnerAdapter = ArrayAdapter.createFromResource(that, R.array.difficulty_spinner, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        difficultySpinner.setAdapter(spinnerAdapter);

        addCustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newWorkoutFragment = new NewExercise();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.hide(thisInstance);
                ft.add(R.id.fragment_container, newWorkoutFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                that.getFragmentManager().executePendingTransactions();

            }
        });


        return fragmentView;
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_workout_menu, menu);
        fragmentMenu = menu;
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_btn:
                // save stuff
                getFragmentManager().popBackStackImmediate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


