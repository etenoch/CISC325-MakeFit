package com.enochtam.cisc325.makefit;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.enochtam.cisc325.makefit.db.DbHelper;
import com.enochtam.cisc325.makefit.db.DbSchema;
import com.enochtam.cisc325.makefit.db.DbSchema.ExerciseEntry;
import com.enochtam.cisc325.makefit.db.DbSchema.WorkoutEntry;
import com.enochtam.cisc325.makefit.db.DbSchema.Workout_ExerciseEntry;
import com.enochtam.cisc325.makefit.models.Exercise;
import com.enochtam.cisc325.makefit.models.Workout;
import com.enochtam.cisc325.makefit.models.WorkoutExerciseLink;
import com.enochtam.cisc325.makefit.models.WorkoutHistoryItem;

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

    // workouts
    public long addWorkout(Workout w){
        ContentValues c = new ContentValues();
        c.put(WorkoutEntry.C_WORKOUT_NAME, w.name);
        c.put(WorkoutEntry.C_DETAILS,w.details);
        c.put(WorkoutEntry.C_DIFFICULTY,w.difficulty);
        long workoutID = insert(WorkoutEntry.T_NAME, c);

        long exerciseID;
        Exercise e;
        if(w.exercises!=null){
            for(WorkoutExerciseLink we:w.exercises){
                if (we.exerciseID==0) exerciseID = addExercise(we.theExercise);
                else exerciseID = we.exerciseID;

                c = new ContentValues();
                c.put(Workout_ExerciseEntry.C_EXERCISE_ID,exerciseID);
                c.put(Workout_ExerciseEntry.C_WORKOUT_ID,workoutID);
                c.put(Workout_ExerciseEntry.C_ORDER, we.order);
                insert(Workout_ExerciseEntry.T_NAME, c);
            }
        }

        return workoutID;
    }

    public void deleteWorkout(Workout w){
        deleteWorkout((int)w.workoutID);
    }

    public void deleteWorkout(int id){
        db.delete(WorkoutEntry.T_NAME, WorkoutEntry._ID + " = ?", new String[]{String.valueOf(id)});
    }

    public List<Workout> getWorkouts(List<Integer> workoutIDs){
        ArrayList<Workout> result = new ArrayList<>();

        String[] projection = {
            WorkoutEntry._ID,
            WorkoutEntry.C_WORKOUT_NAME,
            WorkoutEntry.C_DETAILS,
            WorkoutEntry.C_DIFFICULTY
        };
        String sortOrder = WorkoutEntry._ID + " ASC";

        Cursor c;

        if(workoutIDs == null){
            c = db.query(WorkoutEntry.T_NAME,projection,null,null,null,null,sortOrder);
        }else{
            String listAsString="(";
            for(Integer x:workoutIDs){
                listAsString+=Integer.toString(x)+", ";
            }
            listAsString = listAsString.substring(0, listAsString.length() - 2);
            listAsString+=")";
            c = db.query(WorkoutEntry.T_NAME,projection,ExerciseEntry._ID+" IN "+listAsString,null,null,null,sortOrder);
        }


        c.moveToFirst();
        while (!c.isAfterLast()) {
            long workoutID = c.getLong(c.getColumnIndexOrThrow(WorkoutEntry._ID));
            String name = c.getString(c.getColumnIndexOrThrow(WorkoutEntry.C_WORKOUT_NAME));
            String details = c.getString(c.getColumnIndexOrThrow(WorkoutEntry.C_DETAILS));
            String difficulty = c.getString(c.getColumnIndexOrThrow(WorkoutEntry.C_DIFFICULTY));

            // get exercises
            List<WorkoutExerciseLink> exercises = getExercisesForWorkout((int) workoutID);

            result.add(new Workout(workoutID,name,details,difficulty,exercises));
            c.moveToNext();
        }
        c.close();
        return result;
    }

    public List<Workout> getWorkouts() {
        return getWorkouts(null);
    }

    // Exercise Table
    public long addExercise(Exercise e){
        ContentValues c = new ContentValues();
        c.put(ExerciseEntry.C_EXERCISE_NAME,e.name);
        c.put(ExerciseEntry.C_DETAILS, e.details);
        c.put(ExerciseEntry.C_TIME, e.time);
        if(e.imageUri!= null) c.put(ExerciseEntry.C_PHOTO, e.imageUri.toString());
        return insert(ExerciseEntry.T_NAME, c);
    }

    public List<Exercise> getExercises(){
        return getExercises(null);
    }

    public List<Exercise> getExercises(List<Integer> exerciseIDs){
        String[] projection = {
                ExerciseEntry._ID,
                ExerciseEntry.C_EXERCISE_NAME,
                ExerciseEntry.C_DETAILS,
                ExerciseEntry.C_TIME
        };

        String sortOrder = ExerciseEntry._ID + " ASC";
        Cursor c;
        if(exerciseIDs == null){
            c = db.query(ExerciseEntry.T_NAME,projection,null,null,null,null,sortOrder);
        }else{
            String listAsString="(";
            for(Integer x:exerciseIDs){
                listAsString+=Integer.toString(x)+", ";
            }
            listAsString = listAsString.substring(0, listAsString.length() - 2);
            listAsString+=")";
            c = db.query(ExerciseEntry.T_NAME,projection,ExerciseEntry._ID+" IN "+listAsString,null,null,null,sortOrder);
        }

        ArrayList<Exercise> result = new ArrayList<>();

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


    // Workout<->Exercise Table
    public List<WorkoutExerciseLink> getExercisesForWorkout(int workoutID){

        Cursor c = db.rawQuery(
                "SELECT " +"A."+Workout_ExerciseEntry._ID+", "
                        +"A."+Workout_ExerciseEntry.C_WORKOUT_ID+", "
                        +"A."+Workout_ExerciseEntry.C_EXERCISE_ID+", "
                        +"A."+Workout_ExerciseEntry.C_ORDER+", "
                        +"B."+ExerciseEntry.C_EXERCISE_NAME+", "
                        +"B."+ExerciseEntry.C_TIME+", "
                        +"B."+ExerciseEntry.C_DETAILS+", "
                        +"B."+ExerciseEntry.C_PHOTO+" "+
                "FROM "+Workout_ExerciseEntry.T_NAME+" AS A " +
                "JOIN "+ExerciseEntry.T_NAME+" AS B " +
                "ON A."+Workout_ExerciseEntry.C_EXERCISE_ID+" = B._ID " +
                "WHERE A."+Workout_ExerciseEntry.C_WORKOUT_ID+" = "+workoutID, new String[]{});

        List<WorkoutExerciseLink> result = new ArrayList<>();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            int link_ID = c.getInt(0);
            int workout_ID = c.getInt(1);
            int exercise_ID = c.getInt(2);
            int order = c.getInt(3);

            String exerciseName = c.getString(4);
            int time = c.getInt(5);
            String details = c.getString(6);
            String photoUri = c.getString(7);
            Uri imageUri;
            if(photoUri!= null && !photoUri.isEmpty()) imageUri = Uri.parse(photoUri);
            else imageUri= null;

            Exercise e = new Exercise(exercise_ID,exerciseName,details,time,imageUri);
            result.add(new WorkoutExerciseLink(link_ID, workout_ID, exercise_ID, order,e));
            c.moveToNext();
        }
        c.close();
        return result;
    }


    // workout history
    public long addWorkoutHistoryItem(WorkoutHistoryItem whi){
        ContentValues cv = new ContentValues();
        cv.put(DbSchema.Workout_HistoryEntry.C_DURATION,whi.duration);
        cv.put(DbSchema.Workout_HistoryEntry.C_TIME_DATE,whi.startTime);
        cv.put(DbSchema.Workout_HistoryEntry.C_WORKOUT_ID, whi.workoutID);
        cv.put(DbSchema.Workout_HistoryEntry.C_WORKOUT_NAME, whi.workoutName);
        return insert(DbSchema.Workout_HistoryEntry.T_NAME,cv);
    }
    public List<WorkoutHistoryItem> getWorkoutHistory(List<Integer> itemIDs){
        ArrayList<WorkoutHistoryItem> result = new ArrayList<>();

        String[] projection = {
                DbSchema.Workout_HistoryEntry._ID,
                DbSchema.Workout_HistoryEntry.C_WORKOUT_NAME,
                DbSchema.Workout_HistoryEntry.C_TIME_DATE,
                DbSchema.Workout_HistoryEntry.C_DURATION,
                DbSchema.Workout_HistoryEntry.C_WORKOUT_ID
        };
        String sortOrder = DbSchema.Workout_HistoryEntry._ID + " DESC";

        Cursor c;

        if(itemIDs == null){
            c = db.query(DbSchema.Workout_HistoryEntry.T_NAME,projection,null,null,null,null,sortOrder);
        }else{
            String listAsString="(";
            for(Integer x:itemIDs){
                listAsString+=Integer.toString(x)+", ";
            }
            listAsString = listAsString.substring(0, listAsString.length() - 2);
            listAsString+=")";
            c = db.query(DbSchema.Workout_HistoryEntry.T_NAME,projection,ExerciseEntry._ID+" IN "+listAsString,null,null,null,sortOrder);
        }

        c.moveToFirst();
        while (!c.isAfterLast()) {
            long historyID = c.getLong(c.getColumnIndexOrThrow(DbSchema.Workout_HistoryEntry._ID));
            String workoutName = c.getString(c.getColumnIndexOrThrow(DbSchema.Workout_HistoryEntry.C_WORKOUT_NAME));
            long workoutID = c.getLong(c.getColumnIndexOrThrow(DbSchema.Workout_HistoryEntry.C_WORKOUT_ID));
            long startTime = c.getLong(c.getColumnIndexOrThrow(DbSchema.Workout_HistoryEntry.C_TIME_DATE));
            long duration = c.getLong(c.getColumnIndexOrThrow(DbSchema.Workout_HistoryEntry.C_DURATION));


            result.add(new WorkoutHistoryItem( historyID, workoutName,workoutID, duration, startTime ));
            c.moveToNext();
        }
        c.close();
        return result;

    }

    public List<WorkoutHistoryItem> getWorkoutHistory(){
        return getWorkoutHistory(null);
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
