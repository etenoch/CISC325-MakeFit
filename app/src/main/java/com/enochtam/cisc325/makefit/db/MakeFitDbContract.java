package com.enochtam.cisc325.makefit.db;


import android.provider.BaseColumns;

public final class MakeFitDbContract {

    public MakeFitDbContract() {}

    public static abstract class WorkoutEntry implements BaseColumns {
        public static final String T_NAME = "Workout";
        public static final String C_NAME_WORKOUT_NAME = "Workout_Name";
        public static final String C_NAME_DETAILS = "Details";
        public static final String C_NAME_DIFFICULTY = "Difficulty";
    }

    public static abstract class ExerciseEntry implements BaseColumns {
        public static final String T_NAME = "Exercise";
        public static final String C_NAME_EXERCISE_NAME = "Exercise_Name";
        public static final String C_NAME_REP_TIME = "Rep_Time";
        public static final String C_NAME_REP_TIME_VALUE = "Rep_Time_Value";
        public static final String C_NAME_DETAILS = "DETAILS";
        public static final String C_NAME_PHOTO = "PHOTO";
    }

    public static abstract class Workout_ExerciseEntry implements BaseColumns {
        public static final String T_NAME = "Workout_Exercise";
        public static final String C_NAME_WORKOUT_ID = "Workout_id";
        public static final String C_NAME_EXERCISE_ID = "Exercise_id";
        public static final String C_NAME_ORDER = "ORDER";
    }

    public static abstract class Workout_HistoryEntry implements BaseColumns {
        public static final String T_NAME = "Workout_History";
        public static final String C_NAME_TIME_DATE = "Time_Date";
        public static final String C_NAME_DURATION = "Duration";
        public static final String C_NAME_WORKOUT_ID = "Workout_id";
        public static final String C_NAME_WORKOUT_NAME = "Workout_Name";
    }

}