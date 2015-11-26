package com.enochtam.cisc325.makefit.models;


public class WorkoutHistoryItem {

    public long workoutHistoryItemID;

    public long duration;
    public long startTime;
    public long workoutID;
    public String workoutName;


    public long timeTracker;


    public boolean selected = false;


    public WorkoutHistoryItem(long workoutID, long startTime) {
        this.workoutID = workoutID;
        this.startTime = startTime;
    }


    public WorkoutHistoryItem(long workoutHistoryItemID, String workoutName, long workoutID, long duration, long startTime) {
        this.workoutName = workoutName;
        this.workoutHistoryItemID = workoutHistoryItemID;
        this.duration = duration;
        this.startTime = startTime;
        this.workoutID = workoutID;
    }
}
