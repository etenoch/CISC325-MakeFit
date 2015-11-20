package com.enochtam.cisc325.makefit;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.enochtam.cisc325.makefit.adapters.DrawerAdapter;
import com.enochtam.cisc325.makefit.events.FragmentChangeEvent;
import com.enochtam.cisc325.makefit.fragments.StartScreen;
import com.enochtam.cisc325.makefit.fragments.WorkoutScreen;
import com.enochtam.cisc325.makefit.util.DrawerItem;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    // toolbar
    public Toolbar toolbar;
    public ActionBar actionBar;

    // hamburger menu
    public RecyclerView drawerRecyclerView;
    public RecyclerView.Adapter drawerAdapter;
    public DrawerLayout drawer;
    public ActionBarDrawerToggle drawerToggle;

    // fragment stuff
    private Fragment pendingFragment;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data.getInstance(this);

        if(getResources().getBoolean(R.bool.portrait_only))   // if phone: portrait only
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        // hamburger menu
        drawerRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        drawerRecyclerView.setHasFixedSize(true);
        drawerAdapter = new DrawerAdapter(DrawerItem.drawerItems);
        drawerRecyclerView.setAdapter(drawerAdapter);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.open_drawer,R.string.close_drawer){
            @Override public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if(pendingFragment!=null) changeFragment();
            }
        };
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


        if (CurrentWorkout.getInstance().workoutActive) pendingFragment = new WorkoutScreen();
        else pendingFragment = new StartScreen();
        changeFragment();

        setupCloseKeyboard(findViewById(R.id.DrawerLayout));
//        Data.getInstance().createTestData();

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    public void setToolbarTitle(String title){
        toolbar.setTitle(title);
    }
    public String getToolbarTitle(){
        return (String) actionBar.getTitle();
    }


    // change fragments
    public void changeFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.fragment_container, pendingFragment).commit();
        pendingFragment = null;
    }


    // functions that close keyboard
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
    public void setupCloseKeyboard(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupCloseKeyboard(innerView);
            }
        }
    }


    @Override public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) getFragmentManager().popBackStack();
        else super.onBackPressed();

    }

    //==== Event Bus Handlers ====//
    public void onEvent(FragmentChangeEvent event){
        pendingFragment = event.nextFragment;
        if (event.changeNow) changeFragment();

    }



}
