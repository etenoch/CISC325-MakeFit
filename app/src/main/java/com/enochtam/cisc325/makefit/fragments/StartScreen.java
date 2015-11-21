package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.events.FragmentChangeEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class StartScreen extends Fragment {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();


    View fragmentView;
    MainActivity that;

    @Bind(R.id.start_workout_btn) Button startWorkoutButton;
    @Bind(R.id.first_name) TextView firstName;
    @Bind(R.id.last_name) TextView lastName;

    public StartScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = (MainActivity) getActivity();
    }

    @Override public void onStart() {
        super.onStart();
        that.setToolbarTitle("MakeFit");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_start_screen, container, false);

        ButterKnife.bind(this,fragmentView);

        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = new WorkoutList();
                EventBus.getDefault().post(new FragmentChangeEvent(f, true));
            }
        });

        firstName.setText(that.prefs.getString("firstname",null));
        lastName.setText(that.prefs.getString("lastname",null));

        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



}

