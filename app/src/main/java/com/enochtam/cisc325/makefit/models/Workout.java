package com.enochtam.cisc325.makefit.models;


import java.util.List;

public class Workout {

    public long workoutID;
    public String name;
    public String details;
    public String difficulty;

    public List<WorkoutExerciseLink> exercises;

    public Workout(long workoutID,String name, String details, String difficulty,List<WorkoutExerciseLink> exercises) {
        this.workoutID = workoutID;
        this.name = name;
        this.details = details;
        this.difficulty = difficulty;
        this.exercises = exercises;
    }
    public Workout(long workoutID,String name, String details, String difficulty) {
        this.workoutID = workoutID;
        this.name = name;
        this.details = details;
        this.difficulty = difficulty;
    }
    public Workout(String name, String details, String difficulty) {
        this.name = name;
        this.details = details;
        this.difficulty = difficulty;
    }

    public void addExercise(WorkoutExerciseLink e){
        exercises.add(e);
    }

    public int getTotalTime(){ // return time in minutes
        int seconds=0;
        if (exercises != null) {
            for (WorkoutExerciseLink e:exercises){
                seconds+=e.theExercise.time;
            }
        }
        return seconds/60;
    }

}
