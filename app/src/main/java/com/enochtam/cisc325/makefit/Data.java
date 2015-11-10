package com.enochtam.cisc325.makefit;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.enochtam.cisc325.makefit.db.DbHelper;
import com.enochtam.cisc325.makefit.db.DbSchema.ExerciseEntry;
import com.enochtam.cisc325.makefit.db.DbSchema.WorkoutEntry;
import com.enochtam.cisc325.makefit.db.DbSchema.Workout_ExerciseEntry;
import com.enochtam.cisc325.makefit.models.Exercise;
import com.enochtam.cisc325.makefit.models.Workout;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private static Data instance = null;

    private DbHelper dbHelper;
    private SQLiteDatabase db;

    private Context context;

    protected Data(Context context){
        this.context= context;
        // db helper
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    protected Data(){
        // db helper
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    public long insert(String tableName, ContentValues c){
        return db.insert(tableName,null,c);
    }


    public List<Workout> getWorkouts(){
        ArrayList<Workout> result = new ArrayList<>();

        String[] projection = {
            WorkoutEntry._ID,
            WorkoutEntry.C_WORKOUT_NAME,
            WorkoutEntry.C_DETAILS,
            WorkoutEntry.C_DIFFICULTY
        };
        String sortOrder = WorkoutEntry._ID + " ASC";

        Cursor c = db.query(
                WorkoutEntry.T_NAME,    // The table to query
                projection,             // The columns to return
                null,                   // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        c.moveToFirst();
        while (!c.isAfterLast()) {
            long workoutID = c.getLong(c.getColumnIndexOrThrow(WorkoutEntry._ID));
            String name = c.getString(c.getColumnIndexOrThrow(WorkoutEntry.C_WORKOUT_NAME));
            String details = c.getString(c.getColumnIndexOrThrow(WorkoutEntry.C_DETAILS));
            String difficulty = c.getString(c.getColumnIndexOrThrow(WorkoutEntry.C_DIFFICULTY));

            result.add(new Workout(workoutID,name,details,difficulty));
            c.moveToNext();
        }
        c.close();
        return result;
    }

    public List<Exercise> getExercises(){
        ArrayList<Exercise> result = new ArrayList<>();

        String[] projection = {
                ExerciseEntry._ID,
                ExerciseEntry.C_EXERCISE_NAME,
                ExerciseEntry.C_DETAILS,
                ExerciseEntry.C_TIME
        };
        String sortOrder = ExerciseEntry._ID + " ASC";

        Cursor c = db.query(
                ExerciseEntry.T_NAME,    // The table to query
                projection,             // The columns to return
                null,                   // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        c.moveToFirst();
        while (!c.isAfterLast()) {
            long id = c.getLong(c.getColumnIndexOrThrow(ExerciseEntry._ID));
            String name = c.getString(c.getColumnIndexOrThrow(ExerciseEntry.C_EXERCISE_NAME));
            String details = c.getString(c.getColumnIndexOrThrow(ExerciseEntry.C_DETAILS));
            int time = c.getInt(c.getColumnIndexOrThrow(ExerciseEntry.C_TIME));

            result.add(new Exercise(id,name,details,time));
            c.moveToNext();
        }
        c.close();
        return result;
    }




    public void createTestData(){

        ContentValues c = new ContentValues();
        c.put(WorkoutEntry.C_WORKOUT_NAME, "Test Workout Name 1");
        c.put(WorkoutEntry.C_DETAILS,"Workout Details 1");
        c.put(WorkoutEntry.C_DIFFICULTY,"Intermediate Difficulty");
        long w_id_1 = insert(WorkoutEntry.T_NAME, c);

        c = new ContentValues();
        c.put(WorkoutEntry.C_WORKOUT_NAME,"Test Workout Name 2");
        c.put(WorkoutEntry.C_DETAILS, "Workout Details 2");
        c.put(WorkoutEntry.C_DIFFICULTY, "Intermediate Difficulty");
        long w_id_2 = insert(WorkoutEntry.T_NAME, c);


        c = new ContentValues();
        c.put(ExerciseEntry.C_EXERCISE_NAME,"Test Exercise Name 1");
        c.put(ExerciseEntry.C_DETAILS,"Details for exercise 1. Reps5");
        c.put(ExerciseEntry.C_TIME, 60);
        long e_id_1 = insert(ExerciseEntry.T_NAME, c);

        c = new ContentValues();
        c.put(ExerciseEntry.C_EXERCISE_NAME,"Test Exercise Name 2");
        c.put(ExerciseEntry.C_DETAILS,"Details for exercise 2. Time5mins");
        c.put(ExerciseEntry.C_TIME, 300);
        long e_id_2 = insert(ExerciseEntry.T_NAME, c);


        c = new ContentValues();
        c.put(Workout_ExerciseEntry.C_EXERCISE_ID,e_id_1);
        c.put(Workout_ExerciseEntry.C_WORKOUT_ID,w_id_1);
        c.put(Workout_ExerciseEntry.C_ORDER, 1);
        insert(Workout_ExerciseEntry.T_NAME, c);

        c = new ContentValues();
        c.put(Workout_ExerciseEntry.C_EXERCISE_ID,e_id_2);
        c.put(Workout_ExerciseEntry.C_WORKOUT_ID,w_id_1);
        c.put(Workout_ExerciseEntry.C_ORDER,2);
        insert(Workout_ExerciseEntry.T_NAME, c);

    }

    public static Data getInstance(Context context){
        if(instance == null) instance = new Data(context);
        return instance;
    }
    public static Data getInstance(){
        if(instance == null) instance = new Data();
        return instance;
    }
}
