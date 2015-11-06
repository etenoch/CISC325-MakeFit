package com.enochtam.cisc325.makefit;


public class Data {

    private static Data instance = null;

    protected Data(){



    }





    public static Data getInstance(){
        if(instance == null) instance = new Data();
        return instance;
    }
}
