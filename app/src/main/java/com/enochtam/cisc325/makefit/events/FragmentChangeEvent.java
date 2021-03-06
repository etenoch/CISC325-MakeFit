package com.enochtam.cisc325.makefit.events;


import android.app.Fragment;

public class FragmentChangeEvent {

    public Fragment nextFragment;
    public boolean changeNow = false;
    public boolean backStack = false;

    public FragmentChangeEvent(Fragment nextFragment) {
        this.nextFragment = nextFragment;
    }
    public FragmentChangeEvent(Fragment nextFragment,boolean changeNow) {
        this.nextFragment = nextFragment;
        this.changeNow = changeNow;
    }

    public void setBackStack(boolean backStack) {
        this.backStack = backStack;
    }
}
