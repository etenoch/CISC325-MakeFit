package com.enochtam.cisc325.makefit.models;


import android.net.Uri;

public class Exercise {

    public long exerciseID;
    public String name;
    public String details;
    public int time;

    public Uri imageUri;

    public Exercise(long exerciseID,String name, String details, int time, Uri imageUri) {
        this.exerciseID = exerciseID;
        this.imageUri = imageUri;
        this.name = name;
        this.details = details;
        this.time = time;
    }

    public Exercise(long exerciseID, String name, String details, int time) {
        this.exerciseID = exerciseID;
        this.name = name;
        this.details = details;
        this.time = time;
    }
    public Exercise(String name, String details, int time) {
        this.name = name;
        this.details = details;
        this.time = time;
    }
}


