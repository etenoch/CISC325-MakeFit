package com.enochtam.cisc325.makefit.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.models.WorkoutHistoryItem;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HistoryDetails extends Fragment{

    public WorkoutHistoryItem historyItem;

    public MainActivity that;
    public View fragmentView;

    @Bind(R.id.workout_name) TextView workoutName;
    @Bind(R.id.start_time) TextView startTime;
    @Bind(R.id.duration) TextView duration;
//    @Bind(R.id.to_workout_btn) Button toWorkoutBtn;

    public boolean changeToolbar = false; // true if NOT on tablet/embedded
    public String prevFragmentTitle;

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

            Date date = new Date(historyItem.startTime*1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd - h:ma");


            startTime.setText("Started at: "+sdf.format(date));
            duration.setText("Duration: "+Long.toString(historyItem.duration)+" seconds");
//
//            toWorkoutBtn.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//
//
//                }
//            });

        }
    }

    @Override public void onStart() {
        super.onStart();
        if(changeToolbar){
            prevFragmentTitle = that.getToolbarTitle();
            that.setToolbarTitle("Workout Details");

            that.drawerToggle.setDrawerIndicatorEnabled(false);
            that.drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getFragmentManager().popBackStackImmediate();
                }
            });
            if (that.actionBar != null) {
                that.actionBar.setDisplayHomeAsUpEnabled(true);
                that.actionBar.setHomeButtonEnabled(true);
            }
            that.drawerToggle.syncState();
            that.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override public void onDetach() {
        if(changeToolbar) {
            if (that.actionBar != null) {
                that.actionBar.setDisplayHomeAsUpEnabled(false);
                that.actionBar.setHomeButtonEnabled(false);
            }
            that.drawerToggle.setDrawerIndicatorEnabled(true);
            that.drawerToggle.setToolbarNavigationClickListener(null);
            that.drawerToggle.syncState();
            that.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            that.setToolbarTitle(prevFragmentTitle);
        }
        super.onDetach();
    }

}
