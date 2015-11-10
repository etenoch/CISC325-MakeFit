package com.enochtam.cisc325.makefit.models;


public class Workout {

    public long workoutID;
    public String name;
    public String details;
    public String difficulty;


    public Workout(long workoutID,String name, String details, String difficulty) {
        this.workoutID = workoutID;
        this.name = name;
        this.details = details;
        this.difficulty = difficulty;
    }
}
