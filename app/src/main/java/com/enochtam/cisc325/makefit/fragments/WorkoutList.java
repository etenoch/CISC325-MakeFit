package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.Data;
import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.adapters.WorkoutsAdapter;
import com.enochtam.cisc325.makefit.events.NewWorkoutEvent;
import com.enochtam.cisc325.makefit.models.Workout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class WorkoutList extends Fragment{

    public Fragment thisInstance;
    public View fragmentView;
    MainActivity that;

    @Bind(R.id.workouts_rv) RecyclerView workoutsRecyclerView;
    @Bind(R.id.add_workout_button) FloatingActionButton addWorkoutButton;

    public RecyclerView.LayoutManager workoutsLayoutManager;
    public WorkoutsAdapter workoutsAdatper;

    public WorkoutList() {
        thisInstance = this;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = (MainActivity) getActivity();
    }

    @Override public void onResume() {
        super.onStart();
        that.setToolbarTitle("Workouts");
    }

    @Override public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_workout_list, container, false);
        ButterKnife.bind(this, fragmentView);

        addWorkoutButton.setClickable(true);
        addWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newWorkoutFragment = new NewWorkout();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.hide(thisInstance);
                ft.add(R.id.fragment_container, newWorkoutFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                getFragmentManager().executePendingTransactions();

            }
        });

        // get data from db
        new AsyncTask<Void, Void, List<Workout>>() {
            @Override protected List<Workout> doInBackground(Void... params) {
                return Data.getInstance(that).getWorkouts();
            }
            @Override protected void onPostExecute(List<Workout> result) {
                WorkoutList.this.dataLoaded(result);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);


        if (fragmentView.findViewById(R.id.container_2)!=null){ // tablet, second container exists
            TextView tv = new TextView(that);
            tv.setText("\n\n\n\n\nNo Workout Selected");
            tv.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            tv.setTextSize(30);
            tv.setGravity(Gravity.CENTER);

            View container_2 = fragmentView.findViewById(R.id.container_2);
            ((LinearLayout)container_2).addView(tv);
        }


        return fragmentView;
    }

    public void dataLoaded(List<Workout> workouts){
        // setup recycler view
        workoutsAdatper = new WorkoutsAdapter(workouts,that,this);
//        workoutsAdatper.setHasStableIds(true);
        workoutsLayoutManager = new LinearLayoutManager(that);

        workoutsRecyclerView.setLayoutManager(workoutsLayoutManager);
        workoutsRecyclerView.setAdapter(workoutsAdatper);

    }

    public void onEvent(NewWorkoutEvent newWorkout){
        workoutsAdatper.addDataItem(newWorkout.workout);
    }

}

