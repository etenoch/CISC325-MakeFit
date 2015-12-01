package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.adapters.WorkoutDetailsExercisesAdapter;
import com.enochtam.cisc325.makefit.models.Exercise;
import com.enochtam.cisc325.makefit.models.Workout;
import com.enochtam.cisc325.makefit.models.WorkoutExerciseLink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WorkoutDetails extends Fragment {

    public Workout workout;

    public View fragmentView;
    MainActivity that;

    String prevFragmentTitle;

    @Bind(R.id.exercises_rv) RecyclerView exercisesRecyclerView;
    RecyclerView.LayoutManager exercisesLayoutManager;
    WorkoutDetailsExercisesAdapter exercisesAdatper;

    @Bind(R.id.workout_name) TextView workoutName;
    @Bind(R.id.workout_difficulty) TextView workoutDifficulty;
    @Bind(R.id.workout_details) TextView workoutDetails;
    @Bind(R.id.workout_estimated_time) TextView estimatedTime;
    @Bind(R.id.start_workout_btn) Button startButton;
    @Bind(R.id.edit_workout_btn) Button editButton;

    @Bind(R.id.ll_1) LinearLayout buttonContainer;

    @Bind(R.id.the_container) RelativeLayout theContainer;

    public boolean changeToolbar = false; // true if NOT on tablet/embedded

    public WorkoutDetails() {
        // Required empty public constructor
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = (MainActivity) getActivity();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
            workoutDifficulty.setText(Html.fromHtml("Difficulty: <b>"+workout.difficulty+"</b>"));
            workoutDetails.setText(workout.details);

            //TODO doesn't work
            Spanned html = Html.fromHtml("Estimated Time: <b>"+Integer.toString(workout.getTotalTime())+"</b> minutes");
            estimatedTime.setText(html);

            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final WorkoutScreen workoutFrag = new WorkoutScreen();
                    workoutFrag.setWorkout(workout);

                    final FragmentManager fm = getFragmentManager();
                    fm.popBackStackImmediate();
                    fm.executePendingTransactions();

                    that.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    FragmentTransaction ft = fm.beginTransaction();
                                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                    ft.replace(R.id.fragment_container, workoutFrag).commit();
                                }
                            }, 300);

                        }
                    });

                }
            });

            List <Exercise> exercises = new ArrayList<>();
            // sort exercises items
            if(workout.exercises!=null){
                Collections.sort(workout.exercises, new Comparator<WorkoutExerciseLink>() {
                    @Override public int compare(WorkoutExerciseLink one, WorkoutExerciseLink two) {
                        if (one.order > two.order) return 1;
                        else if (one.order < two.order) return 1;
                        return 0;
                    }
                });
                for (WorkoutExerciseLink wel:workout.exercises) exercises.add(wel.theExercise);
            }

            exercisesLayoutManager = new LinearLayoutManager(that);
            exercisesAdatper = new WorkoutDetailsExercisesAdapter(exercises,that,this);

            exercisesRecyclerView.setLayoutManager(exercisesLayoutManager);
            exercisesRecyclerView.setAdapter(exercisesAdatper);

        }
    }

    public void hideViewsForPreview(){
        workoutName.setVisibility(View.GONE);
        buttonContainer.setVisibility(View.GONE);
    }

}
