package com.enochtam.cisc325.makefit.events;


import android.app.Fragment;

public class FragmentChangeEvent {

    public Fragment nextFragment;

    public FragmentChangeEvent(Fragment nextFragment) {
        this.nextFragment = nextFragment;
    }
}
