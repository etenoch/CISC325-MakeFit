package com.enochtam.cisc325.makefit.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enochtam.cisc325.makefit.Data;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.adapters.WorkoutsAdapter;
import com.enochtam.cisc325.makefit.models.Workout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WorkoutList extends Fragment{

    View fragmentView;
    Activity that;

    @Bind(R.id.workouts_rv) RecyclerView workoutsRecyclerView;

    RecyclerView.LayoutManager workoutsLayoutManager;
    WorkoutsAdapter workoutsAdatper;

    public WorkoutList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_workout_list, container, false);
        ButterKnife.bind(this, fragmentView);

        // get data from db
        new AsyncTask<Void, Void, List<Workout>>() {
            @Override protected List<Workout> doInBackground(Void... params) {
                return Data.getInstance(that).getWorkouts();
            }
            @Override protected void onPostExecute(List<Workout> result) {
                WorkoutList.this.dataLoaded(result);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

        return fragmentView;
    }

    public void dataLoaded(List<Workout> workouts){
        // setup recycler view
        workoutsAdatper = new WorkoutsAdapter(workouts);
//        workoutsAdatper.setHasStableIds(true);
        workoutsLayoutManager = new LinearLayoutManager(that);

        workoutsRecyclerView.setLayoutManager(workoutsLayoutManager);
        workoutsRecyclerView.setAdapter(workoutsAdatper);

    }

}

