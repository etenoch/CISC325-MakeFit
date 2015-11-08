package com.enochtam.cisc325.makefit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.enochtam.cisc325.makefit.adapters.DrawerAdapter;
import com.enochtam.cisc325.makefit.events.FragmentChangeEvent;
import com.enochtam.cisc325.makefit.fragments.StartScreen;
import com.enochtam.cisc325.makefit.util.DrawerItem;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    // toolbar
    Toolbar toolbar;

    // hamburger menu
    private RecyclerView drawerRecyclerView;
    private RecyclerView.Adapter drawerAdapter;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;

    // fragment stuff
    private Fragment pendingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data.getInstance(this);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
                if(pendingFragment!=null) changeFramgent();
            }
        };
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


        // load first fragment
        pendingFragment = new StartScreen();
        changeFramgent();


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


    public void changeFramgent(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.fragment_container, pendingFragment).commit();
        pendingFragment = null;
    }

    //==== Event Bus Handlers ====//
    public void onEvent(FragmentChangeEvent event){
        pendingFragment = event.nextFragment;
    }



}
