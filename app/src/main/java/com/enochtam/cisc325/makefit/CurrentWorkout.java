package com.enochtam.cisc325.makefit;


import android.os.CountDownTimer;

import com.enochtam.cisc325.makefit.models.Exercise;
import com.enochtam.cisc325.makefit.models.Workout;
import com.enochtam.cisc325.makefit.models.WorkoutExerciseLink;

import java.util.Collections;
import java.util.Comparator;

public class CurrentWorkout {

    public boolean workoutActive = false;

    public Workout currentWorkout;
    public Exercise currentExercise;
    public int currentExerciseIndex = 0;

    public boolean autoAdvance=false;

    public long startTime = -1;
    public long exerciseStartTime = -1;

    public long runningWorkoutTime = 0;


    public CountDownTimer exerciseTimer;

    public void startNewWorkout(Workout w){
        currentWorkout = w;
        Collections.sort(currentWorkout.exercises, new Comparator<WorkoutExerciseLink>() {
            @Override
            public int compare(WorkoutExerciseLink lhs, WorkoutExerciseLink rhs) {
                if (lhs.order > rhs.order) return 1;
                if (lhs.order < rhs.order) return -1;
                return 0;
            }
        });
        startTime = getEpochTime();
        setCurrentExercise(currentWorkout.exercises.get(0).theExercise);
    }


    public void setCurrentExercise(Exercise exercise){
        exerciseStartTime = getEpochTime();
        currentExercise = exercise;
    }

    public Exercise nextExercise(){
        currentExerciseIndex +=1;
        Exercise nextExercise = currentWorkout.exercises.get(currentExerciseIndex).theExercise;
        setCurrentExercise(nextExercise);
        return nextExercise;
    }

    public Exercise peekExercise(){
        return currentWorkout.exercises.get(currentExerciseIndex+1).theExercise;
    }



    public void reset(){
        currentWorkout = null;
        currentExercise = null;
        startTime = -1;
        exerciseStartTime = -1;
        currentExerciseIndex = 0;
        runningWorkoutTime = 0;
        workoutActive = false;
    }


    public Long getEpochTime(){
        return System.currentTimeMillis() / 1000L;
    }


    // singleton stuff
    private static CurrentWorkout instance = null;
    protected CurrentWorkout() {
    }
    public static CurrentWorkout getInstance() {
        if(instance == null) instance = new CurrentWorkout();
        return instance;
    }
}