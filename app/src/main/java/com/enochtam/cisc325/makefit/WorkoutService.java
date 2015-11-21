package com.enochtam.cisc325.makefit;


import android.app.IntentService;
import android.content.Intent;

public class WorkoutService extends IntentService {

    public WorkoutService(){
        super("workout-service");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        
    }
}
