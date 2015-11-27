package com.enochtam.cisc325.makefit.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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
    @Bind(R.id.photo_btn) Button photoButton;

    Uri imageUri;

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
//        EventBus.getDefault().register(this);
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

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Set Exercise Photo")
                        .setItems(new String[]{"Pick Photo", "Take Photo"}, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 1) {
                                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(takePicture, 0);//zero can be replaced with any action code
                                } else {
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
                                }
                            }
                        });
                builder.create().show();
            }
        });

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
            Toast.makeText(that, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }else{

            int time_seconds = Integer.parseInt(time.getText().toString()) * 60;
            final Exercise newExercise = new Exercise(
                    exerciseName.getText().toString(),
                    instructions.getText().toString(),
                    time_seconds
            );
            if (imageUri!=null) newExercise.imageUri = imageUri;

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


    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == -1) NewExercise.this.imageUri = imageReturnedIntent.getData();
                break;
            case 1:
                if(resultCode == -1) NewExercise.this.imageUri = imageReturnedIntent.getData();
                break;
        }
    }

    public void exerciseSaved(Exercise newExercise){
        EventBus.getDefault().post(new NewExerciseEvent(newExercise));
        FragmentManager fm =getFragmentManager();
        if (fm!=null) fm.popBackStackImmediate();
    }


}

