package com.enochtam.cisc325.makefit;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity{

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

    // notification
    public Intent notiIntent;
    public PendingIntent pendingIntent;
    public NotificationManager notificationManager;
    public Notification.Builder nBuilder;

    public SharedPreferences prefs;

    public Bitmap profileBitmap;

    public boolean workoutActive;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workoutActive=false;

        Data.getInstance(this);
        prefs = getSharedPreferences("com.enochtam.cisc325.makefit", MODE_PRIVATE);

        if(getResources().getBoolean(R.bool.portrait_only))   // if phone: portrait only
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // notification
        notiIntent = new Intent(this, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, notiIntent, 0);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        // hamburger menu
        drawerRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        drawerRecyclerView.setHasFixedSize(true);
        drawerAdapter = new DrawerAdapter(DrawerItem.drawerItems,this);
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


        if (workoutActive) pendingFragment = new WorkoutScreen();
        else pendingFragment = new StartScreen();
        changeFragment();

        setupCloseKeyboard(findViewById(R.id.DrawerLayout));

    }

    @Override protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            Intent introAct = new Intent(this,IntroActivity.class);
            startActivity(introAct);
        }
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


    public void setProfileBitmap(Uri uri){
        try{
            Bitmap bm = getBitmapFromUri(this,uri);

            profileBitmap = scaleBitmap(bm);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Bitmap scaleBitmap(Bitmap bm){
        int maxWidth = 300;
        int maxHeight = 300;

        int width = bm.getWidth();
        int height = bm.getHeight();

        if (width > height) { // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        } else if (height > width) { // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        } else { // square
            height = maxHeight;
            width = maxWidth;
        }
        return Bitmap.createScaledBitmap(bm, width, height, true);
    }

    public static Bitmap getBitmapFromUri(Context c, Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = c.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
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
    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

        //If a layout container, iterate over children
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

    // notifications
    public void showNotification(String title, String subText){
        if (nBuilder == null){
            nBuilder = new Notification.Builder(this)
                    .setContentTitle(title)
                    .setContentText(subText)
                    .setSmallIcon(R.mipmap.ic_vector)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true);
        }else{
            nBuilder.setContentTitle(title);
            nBuilder.setContentText(subText);
        }
        notificationManager.notify(0, nBuilder.build());
    }
    public void hideNotification(){
        notificationManager.cancelAll();
        nBuilder = null;
    }



    //==== Event Bus Handlers ====//
    public void onEvent(FragmentChangeEvent event){
        pendingFragment = event.nextFragment;
        if (event.changeNow) changeFragment();

    }

}
