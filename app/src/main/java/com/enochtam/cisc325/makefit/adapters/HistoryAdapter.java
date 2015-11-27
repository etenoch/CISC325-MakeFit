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
import com.enochtam.cisc325.makefit.fragments.HistoryDetails;
import com.enochtam.cisc325.makefit.fragments.WorkoutHistory;
import com.enochtam.cisc325.makefit.models.WorkoutHistoryItem;

import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    public List<WorkoutHistoryItem> historyItems;

    public static MainActivity that;
    public static WorkoutHistory workoutHistoryFragment;

    public static WorkoutHistoryItem previousHistoryItem;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public WorkoutHistoryItem historyItem;

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

        public void setHistoryItem(WorkoutHistoryItem h){
            historyItem = h;
        }

        @Override public void onClick(View view) {
            HistoryDetails fragment = new HistoryDetails();
            fragment.setHistoryItem(historyItem);

            FragmentTransaction ft = workoutHistoryFragment.getFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            LinearLayout ll = (LinearLayout)workoutHistoryFragment.fragmentView.findViewById(R.id.container_2);
            if (ll!=null) { // tablet, second container exists
                if (ll.getChildCount() > 0) ll.removeAllViews();
                ft.replace(R.id.container_2, fragment);

                historyItem.selected = true;
                if (previousHistoryItem!=null && previousHistoryItem!=historyItem) previousHistoryItem.selected = false;
                previousHistoryItem = historyItem;
                workoutHistoryFragment.historyAdatper.notifyDataSetChanged();

//                fragment.changeToolbar = false;
            }else {
                ft.hide(workoutHistoryFragment.thisInstance);
                ft.add(R.id.fragment_container, fragment);
//                fragment.changeToolbar = true;
            }

            ft.addToBackStack(null);
            ft.commit();
            workoutHistoryFragment.getFragmentManager().executePendingTransactions();
            fragment.populateViews();
        }

    }// class ViewHolder

    public HistoryAdapter(List<WorkoutHistoryItem> historyItems, MainActivity context, WorkoutHistory fragment){
        this.historyItems = historyItems;
        that = context;
        workoutHistoryFragment = fragment;
    }


    public void addDataItem(WorkoutHistoryItem workout){
        this.historyItems.add(workout);
        notifyDataSetChanged();
    }

    @Override public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item,parent,false);

        ViewHolder viewHolder = new ViewHolder(v);
//        viewHolder.setIsRecyclable(false);
        return viewHolder;
    }

    @Override public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        holder.workoutName.setText(historyItems.get(position).workoutName);
        holder.setHistoryItem(historyItems.get(position));

        if(historyItems.get(position).selected) holder.setSelectedBackground(true);
        else holder.setSelectedBackground(false);


    }

    @Override public int getItemCount() {
        return historyItems.size();
    }


}