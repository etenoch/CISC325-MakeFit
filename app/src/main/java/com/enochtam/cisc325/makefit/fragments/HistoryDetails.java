package com.enochtam.cisc325.makefit.fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.Data;
import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.models.Workout;
import com.enochtam.cisc325.makefit.models.WorkoutHistoryItem;
import com.enochtam.cisc325.makefit.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HistoryDetails extends Fragment{

    public WorkoutHistoryItem historyItem;

    public MainActivity that;
    public View fragmentView;

    @Bind(R.id.workout_name) TextView workoutName;
    @Bind(R.id.start_time) TextView startTime;
    @Bind(R.id.duration) TextView duration;

    @Bind(R.id.workout_preview_label) TextView workoutPreviewLabel;
    @Bind(R.id.workout_details_fragment_container) LinearLayout workoutDetailsFragmentContainer;
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


            duration.setText(Html.fromHtml("Duration: <b>" + Utils.formatDuration((int)historyItem.duration) + "</b>"));

            workoutPreviewLabel.setVisibility(View.INVISIBLE);
            if (historyItem.workoutID != 0){
                new AsyncTask<Void, Void, List<Workout> >() {
                    @Override protected List<Workout>  doInBackground(Void... params) {
                        List<Integer> theWorkout = new ArrayList<>();
                        theWorkout.add((int)historyItem.workoutID);
                        return Data.getInstance(that).getWorkouts(theWorkout);
                    }

                    @Override
                    protected void onPostExecute(List<Workout> historyItems) {
                        HistoryDetails.this.onWorkoutDataLoaded(historyItems);
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

            }

        }
    }

    public void onWorkoutDataLoaded(List<Workout> w){
        if (w.size() >= 1){
            Workout workout = w.get(0);

    //            toWorkoutBtn.setOnClickListener(new View.OnClickListener() {
    //                @Override public void onClick(View v) {
    //
    //                }
    //            });

            workoutPreviewLabel.setVisibility(View.VISIBLE);

            final WorkoutDetails workoutDetails = new WorkoutDetails();
            workoutDetails.setWorkout(workout);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.replace(R.id.workout_details_fragment_container, workoutDetails).commit();

            that.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            workoutDetails.hideViewsForPreview();
                        }
                    }, 300);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            workoutDetails.populateViews();
                        }
                    }, 600);
                }
            });

        }

    }


    @Override
    public void onStart() {
        super.onStart();
        if (changeToolbar) {
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
