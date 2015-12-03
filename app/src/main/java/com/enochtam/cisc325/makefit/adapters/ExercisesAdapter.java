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
import com.enochtam.cisc325.makefit.fragments.ExerciseDetails;
import com.enochtam.cisc325.makefit.fragments.ExercisesList;
import com.enochtam.cisc325.makefit.models.Exercise;

import java.util.List;


public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder> {

    public List<Exercise> exerciseItems;

    public static MainActivity that;
    public static ExercisesList exerciseListFragment;

    public static Exercise previousExercise;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Exercise exercise;

        TextView exerciseName;

        public View thisView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            exerciseName = (TextView) itemView.findViewById(R.id.exercise_name);
            thisView = itemView;
        }

        public void setSelectedBackground(boolean selected){
            if (selected) thisView.setBackgroundResource(R.drawable.selected_border_left);
            else thisView.setBackgroundResource(0);
        }

        public void setExercise(Exercise w){
            exercise = w;
        }

        @Override public void onClick(View view) {
            ExerciseDetails fragment = new ExerciseDetails();
            fragment.setExercise(exercise);

            FragmentTransaction ft = exerciseListFragment.getFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            LinearLayout ll = (LinearLayout)exerciseListFragment.fragmentView.findViewById(R.id.container_2);
            if (ll!=null) { // tablet, second container exists
                if (ll.getChildCount() > 0) ll.removeAllViews();
                ft.replace(R.id.container_2, fragment);

                exercise.selected = true;
                if (previousExercise!=null && previousExercise!=exercise) previousExercise.selected = false;
                previousExercise = exercise;
                exerciseListFragment.exercisesAdatper.notifyDataSetChanged();

                fragment.changeToolbar = false;
            }else {
                ft.hide(exerciseListFragment.thisInstance);
                ft.add(R.id.fragment_container, fragment);
                fragment.changeToolbar = true;
            }

            ft.addToBackStack(null);
            ft.commit();
            exerciseListFragment.getFragmentManager().executePendingTransactions();
            fragment.populateViews();
        }

    }// class ViewHolder

    public ExercisesAdapter(List<Exercise> exerciseItems, MainActivity context, ExercisesList fragment){
        this.exerciseItems = exerciseItems;
        that = context;
        exerciseListFragment = fragment;
    }


    public void addDataItem(Exercise workout){
        this.exerciseItems.add(workout);
        notifyDataSetChanged();
    }

    @Override public ExercisesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list_item,parent,false);

        ViewHolder viewHolder = new ViewHolder(v);
//        viewHolder.setIsRecyclable(false);
        return viewHolder;
    }

    @Override public void onBindViewHolder(ExercisesAdapter.ViewHolder holder, int position) {
        holder.exerciseName.setText(exerciseItems.get(position).name);
        holder.setExercise(exerciseItems.get(position));

        if(exerciseItems.get(position).selected) holder.setSelectedBackground(true);
        else holder.setSelectedBackground(false);


    }

    @Override public int getItemCount() {
        return exerciseItems.size();
    }


}