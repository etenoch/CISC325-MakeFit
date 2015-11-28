package com.enochtam.cisc325.makefit.adapters;


import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.events.FragmentChangeEvent;
import com.enochtam.cisc325.makefit.util.DrawerItem;

import java.util.List;

import de.greenrobot.event.EventBus;


public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<DrawerItem> drawerItems;

    private static MainActivity that;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int holderID;

        TextView itemTitleView;
        ImageView iconView;

        TextView nameTextView;
        TextView exerciseDetails;
        ImageView profileImageView;


        public ViewHolder(View itemView,int ViewType) {
            super(itemView);
            itemView.setClickable(true);

            if(ViewType == TYPE_ITEM) {
                itemView.setOnClickListener(this);
                iconView = (ImageView) itemView.findViewById(R.id.navIcon);
                itemTitleView = (TextView) itemView.findViewById(R.id.rowText);
                holderID = 1;
            }else if(ViewType == TYPE_HEADER) {
                profileImageView = (ImageView) itemView.findViewById(R.id.profile_image);
                nameTextView = (TextView) itemView.findViewById(R.id.name);
                exerciseDetails = (TextView) itemView.findViewById(R.id.exercise_details);
                holderID = 0;
            }
        }

        @Override public void onClick(View view) {
            final Activity host = (Activity) view.getContext();
            DrawerLayout dl = (DrawerLayout) host.findViewById(R.id.DrawerLayout);
            dl.closeDrawers();

            Object fragment = view.findViewById(R.id.rowText).getTag();

            Fragment f;
            try{
                Class c =  Class.forName("com.enochtam.cisc325.makefit.fragments."+fragment.toString());
                f = (Fragment) c.newInstance();
                EventBus.getDefault().post(new FragmentChangeEvent(f));
            }catch (ClassNotFoundException e){
                System.out.println(e.getMessage());
            }catch (InstantiationException e){
                System.out.println(e.getMessage());
            }catch (IllegalAccessException e){
                System.out.println(e.getMessage());
            }
        }

    }// class ViewHolder

    public DrawerAdapter(List<DrawerItem> drawerItems,MainActivity that){
        this.drawerItems = drawerItems;
        this.that = that;
    }

    @Override public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row_item,parent,false);
            return new ViewHolder(v,viewType);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header,parent,false);
            return new ViewHolder(v,viewType);
        }
        return null;
    }

    @Override public void onBindViewHolder(DrawerAdapter.ViewHolder holder, int position) {
        if(holder.holderID ==1) {
            holder.itemTitleView.setText(drawerItems.get(position - 1).title); // Setting the Text with the array of our Titles
            holder.itemTitleView.setTag(drawerItems.get(position - 1).fragmentClassName);
            holder.iconView.setImageResource(drawerItems.get(position - 1).icon);
        }else{
            holder.nameTextView.setText(that.prefs.getString("firstname", null)+" "+that.prefs.getString("lastname", null));
            holder.exerciseDetails.setText("Some Exercise Stats");

            String uriString = that.prefs.getString("imageuri", null);

            if (uriString!=null && !uriString.isEmpty()){
                Uri uri = Uri.parse(uriString);
                if(that.profileBitmap==null)that.setProfileBitmap(uri);
                holder.profileImageView.setImageBitmap(that.profileBitmap);
            }else{
                holder.profileImageView.setImageResource(R.drawable.ic_person_placeholder);
            }
        }
    }

    @Override public int getItemCount() {
        return drawerItems.size()+1;
    }

    @Override public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}