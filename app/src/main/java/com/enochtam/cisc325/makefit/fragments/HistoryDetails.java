package com.enochtam.cisc325.makefit.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.models.WorkoutHistoryItem;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HistoryDetails extends Fragment{

    public WorkoutHistoryItem historyItem;

    public MainActivity that;
    public View fragmentView;

    @Bind(R.id.workout_name) TextView workoutName;
    @Bind(R.id.start_time) TextView startTime;
    @Bind(R.id.duration) TextView duration;

    public HistoryDetails(){
        // empty
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = (MainActivity) getActivity();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.history_item_details, container, false);
        ButterKnife.bind(this, fragmentView);


        return fragmentView;
    }

    public void setHistoryItem(WorkoutHistoryItem historyItem){
        this.historyItem = historyItem;
    }

    public void populateViews(){
        if (historyItem!=null){
            workoutName.setText(historyItem.workoutName);
            startTime.setText("Start time: "+Long.toString(historyItem.startTime)+" unix");
            duration.setText("Duration: "+Long.toString(historyItem.duration)+" seconds");


        }
    }

}
