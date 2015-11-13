package com.enochtam.cisc325.makefit.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.enochtam.cisc325.makefit.db.DbSchema.ExerciseEntry;
import com.enochtam.cisc325.makefit.db.DbSchema.WorkoutEntry;
import com.enochtam.cisc325.makefit.db.DbSchema.Workout_ExerciseEntry;
import com.enochtam.cisc325.makefit.db.DbSchema.Workout_HistoryEntry;


public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "MakeFit.db";

    private static final String SQL_CREATE_T1 =
            "CREATE TABLE " + WorkoutEntry.T_NAME + " (" +
                WorkoutEntry._ID + " INTEGER PRIMARY KEY," +
                WorkoutEntry.C_WORKOUT_NAME +" TEXT,"+
                WorkoutEntry.C_DETAILS +" TEXT,"+
                WorkoutEntry.C_DIFFICULTY +" TEXT"+
            " )";
    private static final String SQL_CREATE_T2 =
            "CREATE TABLE " + ExerciseEntry.T_NAME + " (" +
                ExerciseEntry._ID + " INTEGER PRIMARY KEY," +
                ExerciseEntry.C_EXERCISE_NAME +" TEXT,"+
                ExerciseEntry.C_TIME +" INTEGER,"+
                ExerciseEntry.C_DETAILS +" TEXT,"+
                ExerciseEntry.C_PHOTO +" BLOB"+
            " )";
    private static final String SQL_CREATE_T3 =
            "CREATE TABLE " + Workout_ExerciseEntry.T_NAME + " (" +
                Workout_ExerciseEntry._ID + " INTEGER PRIMARY KEY," +
                Workout_ExerciseEntry.C_WORKOUT_ID +" INTEGER,"+
                Workout_ExerciseEntry.C_EXERCISE_ID +" INTEGER,"+
                Workout_ExerciseEntry.C_ORDER +" INTEGER"+
            " )";
    private static final String SQL_CREATE_T4 =
            "CREATE TABLE " + Workout_HistoryEntry.T_NAME + " (" +
                Workout_HistoryEntry._ID + " INTEGER PRIMARY KEY," +
                Workout_HistoryEntry.C_TIME_DATE +" INTEGER,"+
                Workout_HistoryEntry.C_DURATION +" INTEGER,"+
                Workout_HistoryEntry.C_WORKOUT_ID +" INTEGER,"+
                Workout_HistoryEntry.C_WORKOUT_NAME +" TEXT"+
            " )";

    private static final String SQL_DELETE_1 =
            "DROP TABLE IF EXISTS " + WorkoutEntry.T_NAME;
    private static final String SQL_DELETE_2 =
            "DROP TABLE IF EXISTS " + ExerciseEntry.T_NAME;
    private static final String SQL_DELETE__3 =
            "DROP TABLE IF EXISTS " + Workout_ExerciseEntry.T_NAME;
    private static final String SQL_DELETE_4 =
            "DROP TABLE IF EXISTS " + Workout_HistoryEntry.T_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_T1);
        db.execSQL(SQL_CREATE_T2);
        db.execSQL(SQL_CREATE_T3);
        db.execSQL(SQL_CREATE_T4);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_1);
        db.execSQL(SQL_DELETE_2);
        db.execSQL(SQL_DELETE__3);
        db.execSQL(SQL_DELETE_4);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}