package com.enochtam.cisc325.makefit.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.models.Workout;

import java.util.List;


public class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsAdapter.ViewHolder> {

    private List<Workout> workoutItems;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView workoutName;
        TextView workoutDifficulty;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            workoutName = (TextView) itemView.findViewById(R.id.workout_name);
            workoutDifficulty = (TextView) itemView.findViewById(R.id.workout_difficulty);
        }

        @Override public void onClick(View view) {

        }

    }// class ViewHolder

    public WorkoutsAdapter(List<Workout> workoutItems){
        this.workoutItems = workoutItems;
    }

//    public void setData(List<Workout> workoutItems){
//        this.workoutItems = workoutItems;
//    }

    @Override public WorkoutsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_card_view,parent,false);

        ViewHolder viewHolder = new ViewHolder(v);
//        viewHolder.setIsRecyclable(false);
        return viewHolder;
    }

    @Override public void onBindViewHolder(WorkoutsAdapter.ViewHolder holder, int position) {
        holder.workoutName.setText(workoutItems.get(position).name);
        holder.workoutDifficulty.setText(workoutItems.get(position).difficulty);
//        holder.itemTitleView.setTag(workoutItems.get(position - 1).fragmentClassName);

    }

    @Override public int getItemCount() {
        return workoutItems.size();
    }


}