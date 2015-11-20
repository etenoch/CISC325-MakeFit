package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.CurrentWorkout;
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
    @Bind(R.id.exercise_progress) ProgressBar exerciseProgress;

    CurrentWorkout currentWorkout;

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
        exerciseProgress.setScaleY(3f);

        that.setToolbarTitle("MakeFit Workout");

        if(workout!= null){
            that.setToolbarTitle("MakeFit: "+workout.name);


            Intent intent = new Intent(that, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(that, 0, intent, 0);
            Notification noti = new Notification.Builder(that)
                    .setContentTitle("MakeFit: "+workout.name)
                    .setContentText("MakeFit Workout").setSmallIcon(R.mipmap.ic_vector)
                    .setContentIntent(pIntent)
                    .setOngoing(true)
                    .build();
            NotificationManager notificationManager = (NotificationManager) that.getSystemService(that.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noti);


            currentWorkout = CurrentWorkout.getInstance();
            currentWorkout.workoutActive = true;
            currentWorkout.startNewWorkout(workout);

            newCountDown(currentWorkout.currentExercise.time*1000);
            exerciseName.setText(currentWorkout.currentExercise.name);


//            timeCounter
//            exerciseCard
//            exerciseName
//            exerciseImage
//            exerciseTime
            prevExerciseBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                }
            });
            nextExerciseBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    currentWorkout.nextExercise();
                    newCountDown(currentWorkout.currentExercise.time * 1000);
                    exerciseName.setText(currentWorkout.currentExercise.name);
                }
            });
//            pauseWorkoutBtn
            endWorkoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    StartScreen startFrag = new StartScreen();

                    currentWorkout.reset();

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


    public void newCountDown(long time){
        if(currentWorkout.exerciseTimer!=null) currentWorkout.exerciseTimer.cancel();
        currentWorkout.exerciseTimer =  new CountDownTimer(time, 1000) {
            long total = -1;
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                if (total == -1) total = secondsLeft + 1;
                exerciseTime.setText("Time remaining: " + secondsLeft+" seconds");
                exerciseProgress.setMax((int)total);
                exerciseProgress.setProgress((int)secondsLeft);
            }
            public void onFinish() {
                exerciseTime.setText("Next Exercise");
                exerciseProgress.setProgress(0);
            }
        }.start();
    }


    public void setWorkout(Workout workout) {
        this.workout = workout;
    }
}
