package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.ButterKnife;

public class StartScreen extends Fragment {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();


    View fragmentView;
    MainActivity that;

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


        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



}

