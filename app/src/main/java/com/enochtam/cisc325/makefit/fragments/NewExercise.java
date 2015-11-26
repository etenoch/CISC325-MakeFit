package com.enochtam.cisc325.makefit.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.enochtam.cisc325.makefit.Data;
import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.events.NewExerciseEvent;
import com.enochtam.cisc325.makefit.models.Exercise;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class NewExercise extends DialogFragment {

    String prevFragmentTitle;

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    View fragmentView;
    MainActivity that;

    @Bind(R.id.exercise_name) EditText exerciseName;
    @Bind(R.id.instructions) EditText instructions;
    @Bind(R.id.exercise_time) EditText time;
    @Bind(R.id.save_btn) Button saveBtn;
    @Bind(R.id.cancel_btn) Button cancelBtn;
    @Bind(R.id.new_exercise_container) RelativeLayout rl_container;

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
//        prevFragmentTitle = that.getToolbarTitle();
//        that.setToolbarTitle("New Exercise");
    }
    @Override public void onDetach() {
//        that.setToolbarTitle(prevFragmentTitle);
        super.onDetach();
    }

    @Override public void onPause() {
        that.hideSoftKeyboard(fragmentView);
        super.onPause();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("New Exercise");

        fragmentView = inflater.inflate(R.layout.fragment_new_exercise, container, false);
        ButterKnife.bind(this, fragmentView);

        that.setupCloseKeyboard(rl_container);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                saveItem();
                that.hideSoftKeyboard(v);
                NewExercise.this.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                that.hideSoftKeyboard(v);
                NewExercise.this.dismiss();
            }
        });

        return fragmentView;
    }

    public void saveItem() {
        if (time.getText().toString().isEmpty() ||
                exerciseName.getText().toString().isEmpty() ||
                instructions.getText().toString().isEmpty()){
            Toast.makeText(that,"Please fill in all fields",Toast.LENGTH_SHORT).show();
        }else{

            int time_seconds = Integer.parseInt(time.getText().toString()) * 60;
            final Exercise newExercise = new Exercise(
                    exerciseName.getText().toString(),
                    instructions.getText().toString(),
                    time_seconds
            );

            new AsyncTask<Void, Void, Long>() {
                @Override
                protected Long doInBackground(Void... params) {
                    return Data.getInstance(that).addExercise(newExercise);
                }

                @Override
                protected void onPostExecute(Long newExerciseID) {
                    newExercise.exerciseID = newExerciseID;
                    NewExercise.this.exerciseSaved(newExercise);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

        }

    }

    public void exerciseSaved(Exercise newExercise){
        EventBus.getDefault().post(new NewExerciseEvent(newExercise));
        getFragmentManager().popBackStackImmediate();
    }


}

