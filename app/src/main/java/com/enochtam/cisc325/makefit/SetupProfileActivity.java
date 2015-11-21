package com.enochtam.cisc325.makefit;

import android.app.Activity;
import android.os.Bundle;

public class SetupProfileActivity extends Activity {

    Data dao;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        dao = Data.getInstance(this);

//        prefs.edit().putBoolean("firstrun", false).commit();


    }

}
