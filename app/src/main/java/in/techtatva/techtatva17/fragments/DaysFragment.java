package in.techtatva.techtatva17.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.activities.EventActivity;
import in.techtatva.techtatva17.activities.MainActivity;
import in.techtatva.techtatva17.adapters.EventsAdapter;
import in.techtatva.techtatva17.application.TechTatva;
import in.techtatva.techtatva17.dialogues.EventDialogue;
import in.techtatva.techtatva17.models.events.EventDetailsModel;
import in.techtatva.techtatva17.models.events.EventsListModel;
import in.techtatva.techtatva17.models.events.ScheduleModel;
import io.realm.Realm;
import io.realm.RealmResults;


public class DaysFragment extends Fragment {
    private static final String ARG_PARAM1 = "day";
    private static final String ARG_PARAM2 = "search";
    private int day;
    private String searchTerm;
    Activity activity;
    private EventsListModel currentDayEvents  = new EventsListModel();
    RecyclerView daysRecyclerView;
    public static EventsAdapter adapter;
    List<EventDetailsModel> events;
    Realm realm = Realm.getDefaultInstance();

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public DaysFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param day  This is the day which is selected by the user.
     * @return A new instance of fragment DaysFragment.
     */
    public static DaysFragment newInstance(int day, String searchTerm) {
        DaysFragment fragment = new DaysFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, day);
        args.putString(ARG_PARAM2, searchTerm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = getArguments().getInt(ARG_PARAM1);
            searchTerm =  getArguments().getString(ARG_PARAM2,"");
        }
        //getDataFromRealm();

        getSearchDataFromRealm(searchTerm);
        Log.i("REALM", String.valueOf(day) );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(container==null)
            Log.i("onCreateView","NULL Container");
        View view = inflater.inflate(R.layout.fragment_days, container, false);
        daysRecyclerView =(RecyclerView) view.findViewById(R.id.days_recycler_view);
        EventsAdapter.FavouriteClickListener favouriteClickListener = new EventsAdapter.FavouriteClickListener() {
            @Override
            public void onItemClick(EventDetailsModel event) {
                //Favourite Clicked
                //TODO : Add the favourite Event to the DB and make the Favourite Icon red
            }
        };

        EventsAdapter.EventClickListener eventClickListener = new EventsAdapter.EventClickListener() {
            @Override
            public void onItemClick(EventDetailsModel event) {

                View view = View.inflate(getContext(), R.layout.activity_event_dialogue, null);
                final BottomSheetDialog dialog = new BottomSheetDialog(getContext());

                String eventID = event.getEventID();

                ScheduleModel schedule = realm.where(ScheduleModel.class).equalTo("eventID",eventID).findFirst();


                TextView eventName = (TextView)view.findViewById(R.id.event_name);
                eventName.setText(event.getEventName());

                TextView eventRound = (TextView)view.findViewById(R.id.event_round);
                eventRound.setText(schedule.getRound());

                TextView eventDate = (TextView)view.findViewById(R.id.event_date);
                eventDate.setText(schedule.getDate());

                TextView eventTime = (TextView)view.findViewById(R.id.event_time);
                eventTime.setText(schedule.getStartTime() + " - " + schedule.getEndTime());

                TextView eventVenue = (TextView)view.findViewById(R.id.event_venue);
                eventVenue.setText(schedule.getVenue());

                TextView eventTeamSize = (TextView)view.findViewById(R.id.event_team_size);
                eventTeamSize.setText(event.getMaxTeamSize());

                TextView eventCategory = (TextView)view.findViewById(R.id.event_category);
                eventCategory.setText(event.getCatName());

                TextView eventContact = (TextView)view.findViewById(R.id.event_contact);
                eventContact.setText(event.getContactName() + " ( " + event.getContactNo() + " )");

                TextView eventDescription = (TextView)view.findViewById(R.id.event_description);
                eventDescription.setText(event.getDescription());




                dialog.setContentView(view);
                dialog.show();



            }
        };

        EventsAdapter.EventLongPressListener eventLongPressListener=new EventsAdapter.EventLongPressListener() {
            @Override
            public void onItemLongPress(EventDetailsModel event) {






            }
        };

        adapter = new EventsAdapter(currentDayEvents, eventClickListener,eventLongPressListener, favouriteClickListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        daysRecyclerView.setLayoutManager(layoutManager);
        daysRecyclerView.setItemAnimator(new DefaultItemAnimator());
        daysRecyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(daysRecyclerView.getContext(),getResources().getConfiguration().orientation);
        daysRecyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }

    public void getDataFromRealm(){

        events = realm.where(EventDetailsModel.class).contains("day",(day+1)+"").findAllSorted("eventName");
        currentDayEvents.setEvents(events);


    }

    public void getSearchDataFromRealm(String text){
        events = realm.where(EventDetailsModel.class).contains("day",(day+1)+"").contains("eventName",text).findAllSorted("eventName");
        currentDayEvents.setEvents(events);
        //adapter.notifyDataSetChanged();

    }





}
