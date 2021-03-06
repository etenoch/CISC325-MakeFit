package com.enochtam.cisc325.makefit.adapters;


import android.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.fragments.WorkoutDetails;
import com.enochtam.cisc325.makefit.fragments.WorkoutList;
import com.enochtam.cisc325.makefit.models.Workout;

import java.util.List;


public class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsAdapter.ViewHolder> {

    public List<Workout> workoutItems;

    public static MainActivity that;
    public static WorkoutList workoutListFragment;

    public static Workout previousWorkout;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Workout workout;

        TextView workoutName;
        TextView workoutDifficulty;

        public View thisView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            workoutName = (TextView) itemView.findViewById(R.id.workout_name);
            workoutDifficulty = (TextView) itemView.findViewById(R.id.workout_difficulty);
            thisView = itemView;
        }

        public void setSelectedBackground(boolean selected){
            if (selected) thisView.setBackgroundResource(R.drawable.selected_border_left);
            else thisView.setBackgroundResource(0);
        }

        public void setWorkout(Workout w){
            workout = w;
        }

        @Override public void onClick(View view) {
            WorkoutDetails fragment = new WorkoutDetails();
            fragment.setWorkout(workout);

            FragmentTransaction ft = workoutListFragment.getFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            LinearLayout ll = (LinearLayout)workoutListFragment.fragmentView.findViewById(R.id.container_2);
            if (ll!=null) { // tablet, second container exists
                if (ll.getChildCount() > 0) ll.removeAllViews();
                ft.replace(R.id.container_2, fragment);

                workout.selected = true;
                if (previousWorkout!=null && previousWorkout!=workout) previousWorkout.selected = false;
                previousWorkout = workout;
                workoutListFragment.workoutsAdatper.notifyDataSetChanged();

                fragment.changeToolbar = false;
            }else {
                ft.hide(workoutListFragment.thisInstance);
                ft.add(R.id.fragment_container, fragment);
                fragment.changeToolbar = true;
            }

            ft.addToBackStack(null);
            ft.commit();
            workoutListFragment.getFragmentManager().executePendingTransactions();
            fragment.populateViews();
        }

    }// class ViewHolder

    public WorkoutsAdapter(List<Workout> workoutItems, MainActivity context, WorkoutList fragment){
        this.workoutItems = workoutItems;
        that = context;
        workoutListFragment = fragment;
    }
    

    public void addDataItem(Workout workout){
        this.workoutItems.add(workout);
        notifyDataSetChanged();
    }

    @Override public WorkoutsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_card_view,parent,false);

        ViewHolder viewHolder = new ViewHolder(v);
//        viewHolder.setIsRecyclable(false);
        return viewHolder;
    }

    @Override public void onBindViewHolder(WorkoutsAdapter.ViewHolder holder, int position) {
        holder.workoutName.setText(workoutItems.get(position).name);
        holder.workoutDifficulty.setText(workoutItems.get(position).difficulty);
        holder.setWorkout(workoutItems.get(position));

        if(workoutItems.get(position).selected) holder.setSelectedBackground(true);
        else holder.setSelectedBackground(false);


    }

    @Override public int getItemCount() {
        return workoutItems.size();
    }


}