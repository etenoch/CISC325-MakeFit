package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.enochtam.cisc325.makefit.Data;
import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.events.NewExerciseEvent;
import com.enochtam.cisc325.makefit.models.Exercise;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class NewExercise extends Fragment {

    String prevFragmentTitle;

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    View fragmentView;
    MainActivity that;

    @Bind(R.id.exercise_name) EditText exerciseName;
    @Bind(R.id.instructions) EditText instructions;
    @Bind(R.id.exercise_time) EditText time;

    public static WorkoutScreen newInstance(String param1, String param2) {
        WorkoutScreen fragment = new WorkoutScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public NewExercise() {
        // Required empty public constructor
    }

    @Override public void onStart() {
        super.onStart();
        prevFragmentTitle = that.getToolbarTitle();
        that.setToolbarTitle("New Exercise");
    }
    @Override public void onDetach() {
        that.setToolbarTitle(prevFragmentTitle);
        super.onDetach();
    }

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        that = (MainActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_exercise_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_new_exercise, container, false);
        ButterKnife.bind(this, fragmentView);

        return fragmentView;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_btn:
                int time_seconds = Integer.parseInt(time.getText().toString())*60;
                final Exercise newExercise= new Exercise(
                        exerciseName.getText().toString(),
                        instructions.getText().toString(),
                        time_seconds
                );

                new AsyncTask<Void, Void, Long>() {
                    @Override protected Long doInBackground(Void... params) {
                        return Data.getInstance(that).addExercise(newExercise);
                    }
                    @Override protected void onPostExecute(Long newExerciseID) {
                        newExercise.exerciseID = newExerciseID;
                        NewExercise.this.exerciseSaved(newExercise);
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void exerciseSaved(Exercise newExercise){
        EventBus.getDefault().post(new NewExerciseEvent(newExercise));
        getFragmentManager().popBackStackImmediate();
    }


}

