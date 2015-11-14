package com.enochtam.cisc325.makefit.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.fragments.NewWorkout;
import com.enochtam.cisc325.makefit.models.Exercise;

import java.util.List;


public class NewWorkoutExercisesAdapter extends RecyclerView.Adapter<NewWorkoutExercisesAdapter.ViewHolder> {

    public List<Exercise> exerciseItems;

    public static MainActivity that;
    public static NewWorkout workoutDetailsFragment;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Exercise exercise;

        TextView exerciseName;
        TextView exerciseTime;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            exerciseName = (TextView) itemView.findViewById(R.id.exercise_name);
            exerciseTime = (TextView) itemView.findViewById(R.id.exercise_time);
        }

        public void setExercise(Exercise e){
            exercise = e;
        }

        @Override public void onClick(View view) {

        }

    }// class ViewHolder

    public NewWorkoutExercisesAdapter(List<Exercise> exerciseItems, MainActivity context, NewWorkout fragment){
        this.exerciseItems = exerciseItems;
        that = context;
        workoutDetailsFragment = fragment;
    }

    public void addDataItem(Exercise exercise){
        this.exerciseItems.add(exercise);
        notifyDataSetChanged();
    }

    @Override public NewWorkoutExercisesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_exercise_row_item,parent,false);

        ViewHolder viewHolder = new ViewHolder(v);
//        viewHolder.setIsRecyclable(false);
        return viewHolder;
    }

    @Override public void onBindViewHolder(NewWorkoutExercisesAdapter.ViewHolder holder, int position) {
        holder.exerciseName.setText(exerciseItems.get(position).name);
        holder.exerciseTime.setText(String.valueOf(exerciseItems.get(position).time/60)+" mins");
        holder.setExercise(exerciseItems.get(position));
    }

    @Override public int getItemCount() {
        return exerciseItems.size();
    }


}