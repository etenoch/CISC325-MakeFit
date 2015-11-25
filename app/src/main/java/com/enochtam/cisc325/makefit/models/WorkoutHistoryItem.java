package com.enochtam.cisc325.makefit.models;


public class WorkoutHistoryItem {

    public long workoutHistoryItemID;

    public long duration;
    public long startTime;
    public long workoutID;
    public String workoutName;


    public long timeTracker;



    public WorkoutHistoryItem(long workoutID, long startTime) {
        this.workoutID = workoutID;
        this.startTime = startTime;
    }



}
