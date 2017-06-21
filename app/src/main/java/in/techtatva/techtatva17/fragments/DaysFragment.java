package in.techtatva.techtatva17.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.adapters.EventsAdapter;
import in.techtatva.techtatva17.models.events.EventDetailsModel;
import in.techtatva.techtatva17.models.events.ScheduleListModel;
import in.techtatva.techtatva17.models.events.ScheduleModel;
import io.realm.Realm;


public class DaysFragment extends Fragment {
    private static final String ARG_PARAM1 = "day";
    private static final String ARG_PARAM2 = "search";
    private static final String ARG_PARAM3 = "category";
    private static final String ARG_PARAM4 = "venue";
    private static final String ARG_PARAM5 = "start";
    private static final String ARG_PARAM6 = "end";
    private static final String ARG_PARAM7 = "filter";
    public static EventsAdapter adapter;
    public List<ScheduleModel> events;
    public List<ScheduleModel> filteredEvents = new ArrayList<>();
    Activity activity;
    RecyclerView daysRecyclerView;
    Realm realm = Realm.getDefaultInstance();
    private int day;
    private String searchTerm;
    private String categoryFilter;
    private String venueFilter;
    private String startTimeFilter;
    private String endTimeFilter;
    private boolean filter;
    private ScheduleListModel currentDayEvents = new ScheduleListModel();

    public DaysFragment() {
        // Required empty public constructor
    }

    public static DaysFragment newInstance(int day, String searchTerm, String categoryFilter, String venueFilter, String startTimeFilter, String endTimeFilter, boolean filter) {
        DaysFragment fragment = new DaysFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, day);
        args.putString(ARG_PARAM2, searchTerm);
        args.putString(ARG_PARAM3, categoryFilter);
        args.putString(ARG_PARAM4, venueFilter);
        args.putString(ARG_PARAM5, startTimeFilter);
        args.putString(ARG_PARAM6, endTimeFilter);
        args.putBoolean(ARG_PARAM7, filter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = getArguments().getInt(ARG_PARAM1);
            searchTerm = getArguments().getString(ARG_PARAM2, "");
            categoryFilter = getArguments().getString(ARG_PARAM3, "All");
            venueFilter = getArguments().getString(ARG_PARAM4, "All");
            startTimeFilter = getArguments().getString(ARG_PARAM5, "12:00");
            endTimeFilter = getArguments().getString(ARG_PARAM6, "9:00");
            filter = getArguments().getBoolean(ARG_PARAM7, false);
        }
        //getDataFromRealm();

        getSearchDataFromRealm(searchTerm, categoryFilter, venueFilter, startTimeFilter, endTimeFilter, filter);
        Log.i("REALM", String.valueOf(day));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container == null)
            Log.i("onCreateView", "NULL Container");
        View view = inflater.inflate(R.layout.fragment_days, container, false);
        daysRecyclerView = (RecyclerView) view.findViewById(R.id.days_recycler_view);
        EventsAdapter.FavouriteClickListener favouriteClickListener = new EventsAdapter.FavouriteClickListener() {
            @Override
            public void onItemClick(ScheduleModel event) {
                //Favourite Clicked
                //TODO : Add the favourite Event to the DB and make the Favourite Icon red
            }
        };

        EventsAdapter.EventClickListener eventClickListener = new EventsAdapter.EventClickListener() {
            @Override
            public void onItemClick(ScheduleModel event) {

                View view = View.inflate(getContext(), R.layout.activity_event_dialogue, null);
                final BottomSheetDialog dialog = new BottomSheetDialog(getContext());

                String eventID = event.getEventID();

                EventDetailsModel schedule = realm.where(EventDetailsModel.class).equalTo("eventID", eventID).findFirst();


                TextView eventName = (TextView) view.findViewById(R.id.event_name);
                eventName.setText(event.getEventName());

                TextView eventRound = (TextView) view.findViewById(R.id.event_round);
                eventRound.setText(event.getRound());

                TextView eventDate = (TextView) view.findViewById(R.id.event_date);
                eventDate.setText(event.getDate());

                TextView eventTime = (TextView) view.findViewById(R.id.event_time);
                eventTime.setText(event.getStartTime() + " - " + event.getEndTime());

                TextView eventVenue = (TextView) view.findViewById(R.id.event_venue);
                eventVenue.setText(event.getVenue());

                TextView eventTeamSize = (TextView) view.findViewById(R.id.event_team_size);
                eventTeamSize.setText(schedule.getMaxTeamSize());

                TextView eventCategory = (TextView) view.findViewById(R.id.event_category);
                eventCategory.setText(event.getCatName());

                TextView eventContact = (TextView) view.findViewById(R.id.event_contact);
                eventContact.setText(schedule.getContactName() + " ( " + schedule.getContactNo() + " )");

                TextView eventDescription = (TextView) view.findViewById(R.id.event_description);
                eventDescription.setText(schedule.getDescription());

                dialog.setContentView(view);
                dialog.show();
            }
        };

        EventsAdapter.EventLongPressListener eventLongPressListener = new EventsAdapter.EventLongPressListener() {
            @Override
            public void onItemLongPress(ScheduleModel event) {

            }
        };

        adapter = new EventsAdapter(currentDayEvents, eventClickListener, eventLongPressListener, favouriteClickListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        daysRecyclerView.setLayoutManager(layoutManager);
        daysRecyclerView.setItemAnimator(new DefaultItemAnimator());
        daysRecyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(daysRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        daysRecyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }

    public void getSearchDataFromRealm(String text, String categoryFilter, String venueFilter, String startTimeFilter, String endTimeFilter, boolean filter) {


        if (filter) {

            Log.d("categoryFilter", categoryFilter);
            Log.d("venueFilter", venueFilter);
            Log.d("startTimeFilter", startTimeFilter);
            Log.d("endTimeFilter", endTimeFilter);

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);

            SimpleDateFormat sdfFilter = new SimpleDateFormat("hh:mm aa", Locale.US);

            Date startDate = null;
            Date endDate = null;

            Date startDateFilter = null;
            Date endDateFilter = null;

            if (categoryFilter.equals("All")) {
                categoryFilter = "";
            }
            if (venueFilter.equals("All")) {
                venueFilter = "";
            }


            events = realm.where(ScheduleModel.class).contains("day", (day + 1) + "").contains("eventName", text).contains("catName", categoryFilter).contains("venue", venueFilter).findAllSorted("eventName");


            for (ScheduleModel schedule : events) {
                try {
                    startDate = sdf.parse(schedule.getStartTime());
                    endDate = sdf.parse(schedule.getEndTime());

                    startDateFilter = sdfFilter.parse(startTimeFilter);
                    endDateFilter = sdfFilter.parse(endTimeFilter);

                    Log.d("TAG", startDateFilter.toString());
                    if (startDate.after(startDateFilter) && endDate.before(endDateFilter)) {
                        filteredEvents.add(schedule);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            currentDayEvents.setData(filteredEvents);
        } else {
            events = realm.where(ScheduleModel.class).contains("day", (day + 1) + "").contains("eventName", text).findAllSorted("eventName");
            currentDayEvents.setData(events);
        }


    }


}



