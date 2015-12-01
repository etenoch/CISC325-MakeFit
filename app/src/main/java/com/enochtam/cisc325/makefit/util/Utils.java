package com.enochtam.cisc325.makefit.util;


public class Utils {

    public static String formatDuration(long duration){
        int minutes = (int) duration / 60;
        int seconds = (int) duration % 60;
        return String.format("%02d:%02d", minutes,seconds);
    }

}
