package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.enochtam.cisc325.makefit.Data;
import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.adapters.NewWorkoutExercisesAdapter;
import com.enochtam.cisc325.makefit.events.NewExerciseEvent;
import com.enochtam.cisc325.makefit.events.NewWorkoutEvent;
import com.enochtam.cisc325.makefit.models.Exercise;
import com.enochtam.cisc325.makefit.models.Workout;
import com.enochtam.cisc325.makefit.models.WorkoutExerciseLink;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class NewWorkout extends Fragment {

    Fragment thisInstance;
    String prevFragmentTitle;

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    View fragmentView;
    MainActivity that;

    ArrayAdapter spinnerAdapter;

    @Bind(R.id.pick_exercise_button) Button pickExerciseButton;
    @Bind(R.id.add_custom_button) Button addCustomButton;

    @Bind(R.id.workout_name) EditText workoutName;
    @Bind(R.id.workout_details) EditText workoutDetails;
    @Bind(R.id.workout_difficulty) Spinner difficultySpinner;

    @Bind(R.id.add_exercise_rv) RecyclerView addExerciseRv;
    RecyclerView.LayoutManager exercisesLayoutManager;
    NewWorkoutExercisesAdapter exercisesAdatper;


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
        EventBus.getDefault().register(this);
    }

    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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


        exercisesLayoutManager = new LinearLayoutManager(that);
        exercisesAdatper = new NewWorkoutExercisesAdapter(new ArrayList<Exercise>(),that,this);
        addExerciseRv.setAdapter(exercisesAdatper);
        addExerciseRv.setLayoutManager(exercisesLayoutManager);


        return fragmentView;
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_workout_menu, menu);
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_btn:
                final Workout newWorkout = new Workout(
                        workoutName.getText().toString(),
                        workoutDetails.getText().toString(),
                        difficultySpinner.getSelectedItem().toString()
                );
                // add exercises
                List<WorkoutExerciseLink> exerciseLinks = new ArrayList<>();
                int counter = 1;
                for (Exercise e : exercisesAdatper.exerciseItems){
                    exerciseLinks.add(new WorkoutExerciseLink(counter,e));
                    counter+=1;
                }
                newWorkout.exercises = exerciseLinks;

                new AsyncTask<Void, Void, Long>() {
                    @Override protected Long doInBackground(Void... params) {
                        return Data.getInstance(that).addWorkout(newWorkout);
                    }
                    @Override protected void onPostExecute(Long newWorkoutID) {
                        newWorkout.workoutID = newWorkoutID;
                        NewWorkout.this.workoutSaved(newWorkout);
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void workoutSaved(Workout newWorkout){
        EventBus.getDefault().post(new NewWorkoutEvent(newWorkout));
        getFragmentManager().popBackStackImmediate();
    }

    public void onEvent(NewExerciseEvent exerciseEvent){
        exercisesAdatper.addDataItem(exerciseEvent.exercise);
    }

}


