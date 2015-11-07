package com.enochtam.cisc325.makefit.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.enochtam.cisc325.makefit.db.MakeFitDbContract.*;


public class MakeFitDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MakeFit.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WorkoutEntry.T_NAME + " (" +
                WorkoutEntry._ID + " INTEGER PRIMARY KEY," +
                WorkoutEntry.C_NAME_WORKOUT_NAME +"TEXT,"+
                WorkoutEntry.C_NAME_DETAILS +"TEXT,"+
                WorkoutEntry.C_NAME_DIFFICULTY +"TEXT,"+
            " )"+
            "CREATE TABLE " + ExerciseEntry.T_NAME + " (" +
                ExerciseEntry._ID + " INTEGER PRIMARY KEY," +
                ExerciseEntry.C_NAME_EXERCISE_NAME +"TEXT,"+
                ExerciseEntry.C_NAME_REP_TIME +"INTEGER,"+
                ExerciseEntry.C_NAME_REP_TIME_VALUE +"INTEGER,"+
                ExerciseEntry.C_NAME_DETAILS +"TEXT,"+
                ExerciseEntry.C_NAME_PHOTO +"BLOB,"+
            " )"+
            "CREATE TABLE " + Workout_ExerciseEntry.T_NAME + " (" +
                Workout_ExerciseEntry._ID + " INTEGER PRIMARY KEY," +
                Workout_ExerciseEntry.C_NAME_WORKOUT_ID +"INTEGER,"+
                Workout_ExerciseEntry.C_NAME_EXERCISE_ID +"INTEGER,"+
                Workout_ExerciseEntry.C_NAME_ORDER +"INTEGER,"+
            " )"+
            "CREATE TABLE " + Workout_HistoryEntry.T_NAME + " (" +
                Workout_HistoryEntry._ID + " INTEGER PRIMARY KEY," +
                Workout_HistoryEntry.C_NAME_TIME_DATE +"INTEGER,"+
                Workout_HistoryEntry.C_NAME_DURATION +"INTEGER,"+
                Workout_HistoryEntry.C_NAME_WORKOUT_ID +"INTEGER,"+
                Workout_HistoryEntry.C_NAME_WORKOUT_NAME +"TEXT,"+
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WorkoutEntry.T_NAME;

    private static final String SQL_CREATE_DATA = "";

    public MakeFitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_DATA);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}