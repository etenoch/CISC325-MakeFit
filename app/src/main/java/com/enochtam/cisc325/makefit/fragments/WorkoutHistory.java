package com.enochtam.cisc325.makefit.fragments;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enochtam.cisc325.makefit.Data;
import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.adapters.HistoryAdapter;
import com.enochtam.cisc325.makefit.models.WorkoutHistoryItem;
import com.enochtam.cisc325.makefit.util.AdapterLink;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WorkoutHistory extends Fragment implements AdapterLink {

    public Fragment thisInstance;
    public View fragmentView;
    MainActivity that;


    @Bind(R.id.history_rv) RecyclerView historyRecyclerview;


    public RecyclerView.LayoutManager historyLayoutManager;
    public HistoryAdapter historyAdatper;


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = (MainActivity) getActivity();
    }

    @Override public void onResume() {
        super.onStart();
        that.setToolbarTitle("Workout History");
    }


    public WorkoutHistory() {
        // Required empty public constructor
        thisInstance = this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this,fragmentView);

        // get data from db
        new AsyncTask<Void, Void, List<WorkoutHistoryItem>>() {
            @Override protected List<WorkoutHistoryItem> doInBackground(Void... params) {
                return Data.getInstance(that).getWorkoutHistory();
            }
            @Override protected void onPostExecute(List<WorkoutHistoryItem> result) {
                WorkoutHistory.this.dataLoaded(result);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);


        return fragmentView;
    }

    public void dataLoaded(List<WorkoutHistoryItem> items){

        historyAdatper = new HistoryAdapter(items,that,this);
        historyLayoutManager = new LinearLayoutManager(that);

        historyRecyclerview.setLayoutManager(historyLayoutManager);
        historyRecyclerview.setAdapter(historyAdatper);


    }

    public RecyclerView getRecyclerView(){
        return historyRecyclerview;
    }
    public RecyclerView.LayoutManager getLayoutManager(){
        return historyLayoutManager;
    }
    public View getFragmentView(){
        return fragmentView;
    }
    public Fragment getThis(){
        return this;
    }


}
