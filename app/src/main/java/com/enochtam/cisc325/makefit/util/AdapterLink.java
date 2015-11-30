package com.enochtam.cisc325.makefit.util;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public interface AdapterLink {

    RecyclerView getRecyclerView();
    RecyclerView.LayoutManager getLayoutManager();
    View getFragmentView();
    FragmentManager getFragmentManager();
    Fragment getThis();


}
