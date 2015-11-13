package com.enochtam.cisc325.makefit.events;


import com.enochtam.cisc325.makefit.models.Exercise;

public class NewExerciseEvent {
    public Exercise exercise;

    public NewExerciseEvent(Exercise exercise) {
        this.exercise = exercise;
    }
}
