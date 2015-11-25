package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.enochtam.cisc325.makefit.Data;
import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.WorkoutService;
import com.enochtam.cisc325.makefit.models.Exercise;
import com.enochtam.cisc325.makefit.models.Workout;
import com.enochtam.cisc325.makefit.models.WorkoutExerciseLink;
import com.enochtam.cisc325.makefit.models.WorkoutHistoryItem;

import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WorkoutScreen extends Fragment {

    private Workout workout;

    private View fragmentView;
    private MainActivity that;

    @Bind(R.id.chronometer) Chronometer chronometer;
    @Bind(R.id.time_counter) TextView timeCounter;
    @Bind(R.id.exercise_card) CardView exerciseCard;
    @Bind(R.id.exercise_name) TextView exerciseName;
    @Bind(R.id.exercise_image) ImageView exerciseImage;
    @Bind(R.id.exercise_time) TextView exerciseTime;
    @Bind(R.id.prev_exercise_btn) Button prevExerciseBtn;
    @Bind(R.id.next_exercise_btn) Button nextExerciseBtn;
    @Bind(R.id.pause_workout_btn) Button pauseWorkoutBtn;
    @Bind(R.id.end_workout_btn) Button endWorkoutBtn;
    @Bind(R.id.exercise_progress) ProgressBar exerciseProgress;

    private CountDownTimer exerciseTimer;
    private Exercise currentExercise;
    private int currentIndex;

    private WorkoutHistoryItem historyItem;

    public WorkoutScreen() {
        // Required empty public constructor
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = (MainActivity)getActivity();

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_workout_screen, container, false);
        ButterKnife.bind(this, fragmentView);
        exerciseProgress.setScaleY(3f);

        return fragmentView;
    }

    @Override public void onStart() {
        super.onStart();

        that.setToolbarTitle("MakeFit Workout");

        if(workout!= null && !that.workoutActive){
            that.setToolbarTitle("MakeFit: " + workout.name);

            startNewWorkout();

            Intent i = new Intent(that, WorkoutService.class);
            that.startService(i);


            prevExerciseBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    changeToPrevExercise();
                }
            });
            nextExerciseBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    changeToNextExercise();
                }
            });
            pauseWorkoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {


                }
            });
            endWorkoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    endWorkout();
                }
            });
        }else{
            // not good
            // should probably do something here
        }

    }


    public void startNewWorkout(){

        Collections.sort(workout.exercises, new Comparator<WorkoutExerciseLink>() {
            @Override
            public int compare(WorkoutExerciseLink lhs, WorkoutExerciseLink rhs) {
                if (lhs.order > rhs.order) return 1;
                if (lhs.order < rhs.order) return -1;
                return 0;
            }
        });
        currentIndex=-1;

        historyItem = new WorkoutHistoryItem(workout.workoutID,getEpochTime());

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                timeCounter.setText(chronometer.getText().toString());
            }
        });

        changeToNextExercise();
    }

    public void changeToNextExercise(){
        currentIndex++;
        if (currentIndex < workout.exercises.size()){
            currentExercise = workout.exercises.get(currentIndex).theExercise;
            setExercise(currentExercise);
        }else currentIndex = workout.exercises.size()-1;
    }

    public void changeToPrevExercise(){
        currentIndex--;
        if (currentIndex >= 0) {
            currentExercise = workout.exercises.get(currentIndex).theExercise;
            setExercise(currentExercise);
        }else currentIndex = 0;
    }

    public void endWorkout(){
        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
        Toast.makeText(that, "Elapsed milliseconds: " + elapsedMillis,Toast.LENGTH_SHORT).show();

        chronometer.stop();
        exerciseTimer.cancel();
        workout = null;
        currentExercise = null;
        currentIndex = -1;
        that.workoutActive = false;

        historyItem.duration  = elapsedMillis/1000;
        new AsyncTask<Void, Void, Long>() {
            @Override protected Long doInBackground(Void... params) {
                return Data.getInstance(that).addWorkoutHistoryItem(historyItem);
            }
            @Override protected void onPostExecute(Long newID) {
                historyItem.workoutHistoryItemID = newID;
                WorkoutScreen.this.historyItemSaved(historyItem);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

    }


    public void setExercise(Exercise e){
        that.showNotification("MakeFit: " + workout.name, e.name);

        if(exerciseTimer!=null) exerciseTimer.cancel();
        exerciseTimer = new CountDownTimer(e.time*1000,1000) {
            long total = -1;
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                if (total == -1) total = secondsLeft + 1;
                exerciseTime.setText("Remaining: " + secondsLeft+" seconds");
                exerciseProgress.setMax((int)total);
                exerciseProgress.setProgress((int)secondsLeft);
            }
            public void onFinish() {
                exerciseTime.setText("Next Exercise");
                exerciseProgress.setProgress(0);
            }
        }.start();

        exerciseName.setText(e.name);
    }


    public void historyItemSaved(WorkoutHistoryItem whi){
        that.hideNotification();

        StartScreen startFrag = new StartScreen();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fragment_container, startFrag).commit();
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public long getEpochTime(){
        return System.currentTimeMillis() / 1000L;
    }

}
