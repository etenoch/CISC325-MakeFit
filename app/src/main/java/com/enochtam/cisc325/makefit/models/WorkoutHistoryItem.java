package com.enochtam.cisc325.makefit.models;


public class WorkoutHistoryItem {

    public long totalTime;
    public long startTime;

    public long timeTracker;

    public long workoutID;


    public WorkoutHistoryItem(long workoutID, long startTime) {
        this.workoutID = workoutID;
        this.startTime = startTime;
    }



}
