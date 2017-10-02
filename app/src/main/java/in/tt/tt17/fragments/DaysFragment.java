package in.tt.tt17.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.tt.tt17.R;
import in.tt.tt17.adapters.EventsAdapter;
import in.tt.tt17.models.events.EventDetailsModel;
import in.tt.tt17.models.events.EventModel;
import in.tt.tt17.models.events.ScheduleModel;
import in.tt.tt17.models.favourites.FavouritesModel;
import in.tt.tt17.resources.IconCollection;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class DaysFragment extends Fragment {
    private static final String ARG_PARAM1 = "day";
    private static final String ARG_PARAM2 = "search";
    private static final String ARG_PARAM3 = "category";
    private static final String ARG_PARAM4 = "venue";
    private static final String ARG_PARAM5 = "start";
    private static final String ARG_PARAM6 = "end";
    private static final String ARG_PARAM7 = "filter";
    public EventsAdapter adapter;
    public List<EventModel> events = new ArrayList<>();
    public List<EventModel> filteredEvents = new ArrayList<>();
    Activity activity;
    RecyclerView daysRecyclerView;
    Realm realm = Realm.getDefaultInstance();
    public int day;
    private String searchTerm;
    private String categoryFilter;
    private String venueFilter;
    private String startTimeFilter;
    private String endTimeFilter;
    private boolean filter;
    private List<EventModel> currentDayEvents = new ArrayList<>();

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
        final View daysView = inflater.inflate(R.layout.fragment_days, container, false);
        daysRecyclerView = (RecyclerView) daysView.findViewById(R.id.days_recycler_view);
        EventsAdapter.FavouriteClickListener favouriteClickListener = new EventsAdapter.FavouriteClickListener() {
            @Override
            public void onItemClick(ScheduleModel event) {
                //Favourite Clicked
            }
        };


         EventsAdapter.EventClickListener eventClickListener = new EventsAdapter.EventClickListener() {
            @Override
            public void onItemClick(final ScheduleModel event, final View view1) {

                final View view = View.inflate(getContext(), R.layout.activity_event_dialogue, null);
                final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
                String eventID = event.getEventID();

                final EventDetailsModel schedule = realm.where(EventDetailsModel.class).equalTo("eventID", eventID).findFirst();

                ImageView eventLogo1 = (ImageView) view.findViewById(R.id.event_logo_image_view);

                IconCollection icons = new IconCollection();
                eventLogo1.setImageResource(icons.getIconResource(activity, event.getCatName()));

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

                TextView eventContactName = (TextView) view.findViewById(R.id.event_contact_name);
                eventContactName.setText(schedule.getContactName() + " : ");

                TextView eventContact = (TextView) view.findViewById(R.id.event_contact);
                eventContact.setText(  schedule.getContactNo());
                eventContact.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

                eventContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + schedule.getContactNo()));

                        getActivity().startActivity(intent);
                    }
                });

                TextView eventDescription = (TextView) view.findViewById(R.id.event_description);
                eventDescription.setText(schedule.getDescription());

                final ImageView deleteIcon = (ImageView)view.findViewById(R.id.event_delete_icon);
                final ImageView favIcon = (ImageView)view.findViewById(R.id.event_fav_icon);
                favIcon.setVisibility(View.GONE);
                String sDay = Integer.toString(day);

                final RealmResults<FavouritesModel> results =    realm.where(FavouritesModel.class).equalTo("id", eventID).contains("day", (day + 1) + "").findAll();

                if(results.size() == 0 ){deleteIcon.setVisibility(View.GONE);}

                deleteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        realm.beginTransaction();
                        results.deleteAllFromRealm();
                        realm.commitTransaction();
                        ImageView favIcon = (ImageView) view1.findViewById(R.id.event_fav_ico);
                        favIcon.setImageResource(R.drawable.ic_fav_deselected);
                        favIcon.setTag("deselected");
                        deleteIcon.setVisibility(View.GONE);
                    }




                });

                dialog.setContentView(view);
                Snackbar.make(view.getRootView().getRootView(),"Swipe up for more", Snackbar.LENGTH_SHORT).show();
                dialog.show();
            }
        };

        EventsAdapter.EventLongPressListener eventLongPressListener = new EventsAdapter.EventLongPressListener() {
            @Override
            public void onItemLongPress(ScheduleModel event) {

            }
        };

        adapter = new EventsAdapter(getActivity(),currentDayEvents, eventClickListener, eventLongPressListener, favouriteClickListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        daysRecyclerView.setLayoutManager(layoutManager);
        daysRecyclerView.setItemAnimator(new DefaultItemAnimator());
        daysRecyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(daysRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        daysRecyclerView.addItemDecoration(dividerItemDecoration);
        return daysView;
    }
    public void getSearchDataFromRealm(String text, String categoryFilter, String venueFilter, String startTimeFilter, String endTimeFilter, boolean filter) {
        if (filter) {
            Log.d("categoryFilter", categoryFilter);
            Log.d("venueFilter", venueFilter);
            Log.d("startTimeFilter", startTimeFilter);
            Log.d("endTimeFilter", endTimeFilter);

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);
            SimpleDateFormat sdfFilter = new SimpleDateFormat("hh:mm aa", Locale.US);

            Date startDate;
            Date endDate;

            Date startDateFilter;
            Date endDateFilter;

            if (categoryFilter.equals("All")) {
                categoryFilter = "";
            }
            if (venueFilter.equals("All")) {
                venueFilter = "";
            }
            RealmResults<ScheduleModel> tempevents;
            tempevents = realm.where(ScheduleModel.class).equalTo("day", (day + 1) + "").contains("eventName", text.trim(),Case.INSENSITIVE).contains("catName", categoryFilter.trim(),Case.INSENSITIVE).contains("venue", venueFilter.trim(), Case.INSENSITIVE).findAllSorted("startTime", Sort.ASCENDING, "eventName", Sort.ASCENDING);

            for (ScheduleModel schedule : tempevents) {
                EventDetailsModel eventDetails = realm.where(EventDetailsModel.class).equalTo("eventID", schedule.getEventID()).findFirst();
                EventModel event = new EventModel(eventDetails, schedule);
                events.add(event);
            }
            for (EventModel schedule : events) {
                try {
                    startDate = sdf.parse(schedule.getStartTime());
                    endDate = sdf.parse(schedule.getEndTime());

                    startDateFilter = sdfFilter.parse(startTimeFilter);
                    endDateFilter = sdfFilter.parse(endTimeFilter);

                    Log.d("TAG", startDateFilter.toString());
                    if ((startDate.after(startDateFilter) ||startDate.equals(startDateFilter)) && (endDate.equals(endDateFilter) || endDate.before(endDateFilter))) {
                        filteredEvents.add(schedule);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            currentDayEvents = (filteredEvents);
        } else {
            RealmResults<ScheduleModel> tempevents;
            Log.d("Day", (day+1)+"");
            tempevents = realm.where(ScheduleModel.class).equalTo("day", (day + 1) + "").findAllSorted("startTime", Sort.ASCENDING, "eventName", Sort.ASCENDING);

            for (ScheduleModel schedule : tempevents) {
                EventDetailsModel eventDetails = realm.where(EventDetailsModel.class).equalTo("eventID", schedule.getEventID()).findFirst();

                if (eventDetails!=null && eventDetails.getEventName().contains(text)){
                    EventModel event = new EventModel(eventDetails, schedule);
                    currentDayEvents.add(event);
                }
            }
        }
    }

}