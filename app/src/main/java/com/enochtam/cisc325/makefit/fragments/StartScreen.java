package com.enochtam.cisc325.makefit.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.enochtam.cisc325.makefit.Data;
import com.enochtam.cisc325.makefit.MainActivity;
import com.enochtam.cisc325.makefit.R;
import com.enochtam.cisc325.makefit.adapters.HistoryAdapter;
import com.enochtam.cisc325.makefit.events.FragmentChangeEvent;
import com.enochtam.cisc325.makefit.models.WorkoutHistoryItem;
import com.enochtam.cisc325.makefit.util.AdapterLink;
import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;
import com.imanoweb.calendarview.DayDecorator;
import com.imanoweb.calendarview.DayView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

public class StartScreen extends Fragment implements AdapterLink {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    List<WorkoutHistoryItem> historyItems;

    View fragmentView;
    MainActivity that;

    @Bind(R.id.start_workout_btn) Button startWorkoutButton;
    @Bind(R.id.first_name) TextView firstName;
    @Bind(R.id.last_name) TextView lastName;
    @Bind(R.id.profile_image) CircleImageView profileImage;
    @Bind(R.id.calendar_view) CustomCalendarView calendarView;

    public RecyclerView historyRecyclerView;
    public RecyclerView.LayoutManager historyLayoutManager;
    public HistoryAdapter historyAdapter;

    public StartScreen() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that = (MainActivity) getActivity();
    }

    @Override public void onStart() {
        super.onStart();
        that.setToolbarTitle("MakeFit");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_start_screen, container, false);

        ButterKnife.bind(this, fragmentView);

        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = new WorkoutList();
                EventBus.getDefault().post(new FragmentChangeEvent(f, true));
            }
        });

        firstName.setText(that.prefs.getString("firstname", null));
        lastName.setText(that.prefs.getString("lastname", null));

        String uriString = that.prefs.getString("imageuri", null);

        if (uriString!=null && !uriString.isEmpty()){
            Uri uri = Uri.parse(uriString);
            if(that.profileBitmap==null)that.setProfileBitmap(uri);
            profileImage.setImageBitmap(that.profileBitmap);
        }else{
            profileImage.setImageResource(R.drawable.ic_person_placeholder);
        }

        new AsyncTask<Void, Void, List<WorkoutHistoryItem> >() {
            @Override protected List<WorkoutHistoryItem>  doInBackground(Void... params) {
                return Data.getInstance(that).getWorkoutHistory();
            }

            @Override
            protected void onPostExecute(List<WorkoutHistoryItem> historyItems) {
                StartScreen.this.dataLoaded(historyItems);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);



        return fragmentView;
    }

    private class ColorDecorator implements DayDecorator {
        @Override public void decorate(DayView cell) {

            int start_day = cell.getDate().getDate();
            int start_year = cell.getDate().getYear();
            int start_month = cell.getDate().getMonth();

            for(WorkoutHistoryItem item : historyItems){
                Date history = new Date(item.startTime*1000L);
                int history_day = history.getDate();
                int history_year = history.getYear();
                int history_month = history.getMonth();

                if( start_day == history_day && start_year == history_year && start_month == history_month ){
                    int color = Color.argb(255, 0, 200, 0);
                    cell.setBackgroundResource(R.drawable.calendar_marker);
                }
            }

        }
    }


    public void dataLoaded(List<WorkoutHistoryItem> historyItems){
        // index history items
        this.historyItems = historyItems;

        // calendar stuff
        final Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        final List<DayDecorator> decorators = new ArrayList<>();
        decorators.add(new ColorDecorator());
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);

        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Toast.makeText(getActivity(), df.format(date), Toast.LENGTH_SHORT).show();

                calendarView.setDecorators(decorators);
                calendarView.refreshCalendar(currentCalendar);
            }

            @Override
            public void onMonthChanged(Date date) {
//                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
//                Toast.makeText(getActivity(), df.format(date), Toast.LENGTH_SHORT).show();
            }
        });


        historyRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.history_rv);
        if (historyRecyclerView!= null){
            historyAdapter = new HistoryAdapter(historyItems,that,this);
            historyAdapter.type = HistoryAdapter.SMALL_CARDS;
            LinearLayoutManager historyLayoutManager = new LinearLayoutManager(that);

            historyRecyclerView.setLayoutManager(historyLayoutManager);
            historyRecyclerView.setAdapter(historyAdapter);
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public RecyclerView getRecyclerView(){
        return historyRecyclerView;
    }
    public RecyclerView.LayoutManager getLayoutManager(){
        return historyLayoutManager;
    }
    public View getFragmentView(){
        return fragmentView;
    }
    public Fragment getThis(){
        return this;
    }



}

