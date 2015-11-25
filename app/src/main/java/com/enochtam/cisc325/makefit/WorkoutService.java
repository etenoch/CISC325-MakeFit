package com.enochtam.cisc325.makefit;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.enochtam.cisc325.makefit.fragments.WorkoutScreen;

public class WorkoutService extends Service {

    public WorkoutService(){
        super();
    }

    public WorkoutScreen screenInstance;

    public void setScreenInstance(WorkoutScreen screenInstance) {
        this.screenInstance = screenInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
