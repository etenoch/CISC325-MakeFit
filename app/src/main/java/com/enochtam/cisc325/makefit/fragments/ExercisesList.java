package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
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
import com.enochtam.cisc325.makefit.adapters.ExercisesAdapter;
import com.enochtam.cisc325.makefit.events.NewExerciseEvent;
import com.enochtam.cisc325.makefit.models.Exercise;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class ExercisesList extends Fragment{

    public Fragment thisInstance;
    public View fragmentView;
    MainActivity that;

    @Bind(R.id.workouts_rv) RecyclerView exercisesRecyclerView;
    @Bind(R.id.add_workout_button) FloatingActionButton addExerciseButton;

    public RecyclerView.LayoutManager exercisesLayoutManager;
    public ExercisesAdapter exercisesAdatper;

    public ExercisesList() {
        thisInstance = this;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = (MainActivity) getActivity();
    }

    @Override public void onResume() {
        super.onStart();
        that.setToolbarTitle("Exercises");
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

        setRetainInstance(true); // still trying to figure out how to handle configuration changes

        ButterKnife.bind(this, fragmentView);

        addExerciseButton.setClickable(true);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newWorkoutFragment = new NewExercise();

                FragmentManager fm = that.getSupportFragmentManager();
                newWorkoutFragment.show(fm, "new_exercise_fragment");
                fm.executePendingTransactions();

            }
        });

        // get data from db
        new AsyncTask<Void, Void, List<Exercise>>() {
            @Override protected List<Exercise> doInBackground(Void... params) {
                return Data.getInstance(that).getExercises();
            }
            @Override protected void onPostExecute(List<Exercise> result) {
                ExercisesList.this.dataLoaded(result);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);


        if (fragmentView.findViewById(R.id.container_2)!=null){ // tablet, second container exists
            TextView tv = new TextView(that);
            tv.setText("\n\n\n\n\nNo Exercise Selected");
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

    public void dataLoaded(List<Exercise> exercises){
        // setup recycler view
        exercisesAdatper = new ExercisesAdapter(exercises,that,this);
//        workoutsAdatper.setHasStableIds(true);
        exercisesLayoutManager = new LinearLayoutManager(that);

        exercisesRecyclerView.setLayoutManager(exercisesLayoutManager);
        exercisesRecyclerView.setAdapter(exercisesAdatper);

    }

    public void onEvent(NewExerciseEvent newExerciseEvent){
        exercisesAdatper.addDataItem(newExerciseEvent.exercise);
        exercisesAdatper.notifyDataSetChanged();
    }

}

