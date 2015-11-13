package com.enochtam.cisc325.makefit.util;


import com.enochtam.cisc325.makefit.R;

import java.util.Arrays;
import java.util.List;

public class DrawerItem {

    public static List<DrawerItem> drawerItems = Arrays.asList(
            new DrawerItem(R.drawable.ic_profile,"MakeFit Profile","StartScreen"),
            new DrawerItem(R.drawable.ic_running_man,"Workouts","WorkoutList"),
            new DrawerItem(R.drawable.ic_history_list,"History","WorkoutHistory"),
            new DrawerItem(R.drawable.ic_settings,"Settings","StartScreen")
    );

    public int icon;
    public String title;
    public String fragmentClassName;

    public DrawerItem(int icon, String title, String fragmentClassName) {
        this.icon = icon;
        this.title = title;
        this.fragmentClassName = fragmentClassName;
    }


}
