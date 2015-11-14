package com.enochtam.cisc325.makefit.models;


public class WorkoutExerciseLink {

    public int linkID;
    public int workoutID;
    public int exerciseID;
    public int order;

    public Exercise theExercise;

    public WorkoutExerciseLink(int linkID, int workoutID, int exerciseID, int order) {
        this.linkID = linkID;
        this.workoutID = workoutID;
        this.exerciseID = exerciseID;
        this.order = order;
    }

    public WorkoutExerciseLink(int linkID, int workoutID, int exerciseID, int order, Exercise e) {
        this.linkID = linkID;
        this.workoutID = workoutID;
        this.exerciseID = exerciseID;
        this.order = order;
        this.theExercise = e;
    }

    public WorkoutExerciseLink(int exerciseID, int order, Exercise e) {
        this.exerciseID = exerciseID;
        this.order = order;
        this.theExercise = e;
    }

    public WorkoutExerciseLink(int order, Exercise e) {
        this.order = order;
        this.theExercise = e;
    }
}
