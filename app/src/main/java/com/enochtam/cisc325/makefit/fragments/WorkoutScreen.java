package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.models.Workout;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WorkoutScreen extends Fragment {


    private Workout workout;

    private View fragmentView;

    private MainActivity that;

    @Bind(R.id.time_counter) TextView timeCounter;
    @Bind(R.id.exercise_card) CardView exerciseCard;
    @Bind(R.id.exercise_name) TextView exerciseName;
    @Bind(R.id.exercise_image) ImageView exerciseImage;
    @Bind(R.id.exercise_time) TextView exerciseTime;
    @Bind(R.id.prev_exercise_btn) Button prevExerciseBtn;
    @Bind(R.id.next_exercise_btn) Button nextExerciseBtn;
    @Bind(R.id.pause_workout_btn) Button pauseWorkoutBtn;
    @Bind(R.id.end_workout_btn) Button endWorkoutBtn;



    public WorkoutScreen() {
        // Required empty public constructor
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = (MainActivity)getActivity();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_workout_screen, container, false);
        ButterKnife.bind(this,fragmentView);

        return fragmentView;
    }

    @Override public void onStart() {
        super.onStart();
        that.setToolbarTitle("MakeFit Workout");

        if(workout!= null){
            that.setToolbarTitle("MakeFit: "+workout.name);
//            timeCounter
//            exerciseCard
//            exerciseName
//            exerciseImage
//            exerciseTime
//            prevExerciseBtn
//            nextExerciseBtn
//            pauseWorkoutBtn
            endWorkoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    StartScreen startFrag = new StartScreen();

                    final FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.replace(R.id.fragment_container, startFrag).commit();

                }
            });
        }else{
            // not good
            // should probably do something here
        }

    }


    public void setWorkout(Workout workout) {
        this.workout = workout;
    }
}
