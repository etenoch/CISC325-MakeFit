package com.enochtam.cisc325.makefit.events;


import com.enochtam.cisc325.makefit.models.Workout;

public class NewWorkoutEvent {

    public Workout workout;

    public NewWorkoutEvent(Workout workout) {
        this.workout = workout;
    }
}
