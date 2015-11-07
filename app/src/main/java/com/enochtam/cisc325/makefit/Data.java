package com.enochtam.cisc325.makefit;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.enochtam.cisc325.makefit.db.MakeFitDbHelper;

public class Data {

    private static Data instance = null;

    private MakeFitDbHelper dbHelper;
    private SQLiteDatabase db;

    private Context context;

    protected Data(Context context){
        this.context= context;
        // db helper
        dbHelper = new MakeFitDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    public long insert(String tableName, ContentValues c){
        return db.insert(tableName,null,c);
    }


    public static Data getInstance(Context context){
        if(instance == null) instance = new Data(context);
        return instance;
    }
}
