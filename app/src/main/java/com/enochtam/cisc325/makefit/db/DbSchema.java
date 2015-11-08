package com.enochtam.cisc325.makefit.db;


import android.provider.BaseColumns;

public final class DbSchema {

    public DbSchema() {}

    public static abstract class WorkoutEntry implements BaseColumns {
        public static final String T_NAME = "Workout";
        public static final String C_WORKOUT_NAME = "Workout_Name";
        public static final String C_DETAILS = "Details";
        public static final String C_DIFFICULTY = "Difficulty";
    }

    public static abstract class ExerciseEntry implements BaseColumns {
        public static final String T_NAME = "Exercise";
        public static final String C_EXERCISE_NAME = "Exercise_Name";
        public static final String C_REP_TIME = "Rep_Time";
        public static final String C_REP_TIME_VALUE = "Rep_Time_Value";
        public static final String C_DETAILS = "DETAILS";
        public static final String C_PHOTO = "PHOTO";
    }

    public static abstract class Workout_ExerciseEntry implements BaseColumns {
        public static final String T_NAME = "Workout_Exercise";
        public static final String C_WORKOUT_ID = "Workout_id";
        public static final String C_EXERCISE_ID = "Exercise_id";
        public static final String C_ORDER = "EXERCISE_ORDER";
    }

    public static abstract class Workout_HistoryEntry implements BaseColumns {
        public static final String T_NAME = "Workout_History";
        public static final String C_TIME_DATE = "Time_Date";
        public static final String C_DURATION = "Duration";
        public static final String C_WORKOUT_ID = "Workout_id";
        public static final String C_WORKOUT_NAME = "Workout_Name";
    }

}