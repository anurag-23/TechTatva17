package in.techtatva.techtatva17.fragments;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.adapters.FavouritesEventsAdapter;
import in.techtatva.techtatva17.models.events.EventDetailsModel;
import in.techtatva.techtatva17.models.favourites.FavouritesModel;
import io.realm.Realm;


public class FavouritesFragment extends Fragment {

    private Realm realm = Realm.getDefaultInstance();
    private List<FavouritesModel> favouritesDay1 =  new ArrayList<>();
    private List<FavouritesModel> favouritesDay2 =  new ArrayList<>();
    private List<FavouritesModel> favouritesDay3 =  new ArrayList<>();
    private List<FavouritesModel> favouritesDay4 =  new ArrayList<>();
    RecyclerView recyclerViewDay1;
    RecyclerView recyclerViewDay2;
    RecyclerView recyclerViewDay3;
    RecyclerView recyclerViewDay4;
    private TextView noEventsDay1;
    private TextView noEventsDay2;
    private TextView noEventsDay3;
    private TextView noEventsDay4;
    public FavouritesFragment() {
        // Required empty public constructor
    }


    public static FavouritesFragment newInstance() {
        FavouritesFragment fragment = new FavouritesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.favourites_fragment);
        favouritesDay1 = realm.copyFromRealm( realm.where(FavouritesModel.class).equalTo("day","1").findAll());
        favouritesDay2 = realm.copyFromRealm( realm.where(FavouritesModel.class).equalTo("day","2").findAll());
        favouritesDay3 = realm.copyFromRealm( realm.where(FavouritesModel.class).equalTo("day","3").findAll());
        favouritesDay4 = realm.copyFromRealm( realm.where(FavouritesModel.class).equalTo("day","4").findAll());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        recyclerViewDay1 = (RecyclerView)view.findViewById(R.id.favourites_day_1_recycler_view);
        recyclerViewDay2 = (RecyclerView)view.findViewById(R.id.favourites_day_2_recycler_view);
        recyclerViewDay3 = (RecyclerView)view.findViewById(R.id.favourites_day_3_recycler_view);
        recyclerViewDay4 = (RecyclerView)view.findViewById(R.id.favourites_day_4_recycler_view);

        noEventsDay1 = (TextView)view.findViewById(R.id.fav_day_1_no_events);
        noEventsDay2 = (TextView)view.findViewById(R.id.fav_day_2_no_events);
        noEventsDay3 = (TextView)view.findViewById(R.id.fav_day_3_no_events);
        noEventsDay4 = (TextView)view.findViewById(R.id.fav_day_4_no_events);

        displayEvents();
        return view;
    }

    public void displayEvents(){

        FavouritesEventsAdapter.EventClickListener eventListener = new  FavouritesEventsAdapter.EventClickListener(){
            @Override
            public void onItemClick(FavouritesModel event) {
                //Event Clicked
                displayBottomSheet(event);
            }
        };

        if(favouritesDay1.isEmpty()){
            recyclerViewDay1.setVisibility(View.GONE);
            ((View)recyclerViewDay1.getParent()).setVisibility(View.GONE);
            noEventsDay1.setVisibility(View.VISIBLE);
            ((View)noEventsDay1.getParent()).setVisibility(View.VISIBLE);

        }else{
            FavouritesEventsAdapter adapter = new FavouritesEventsAdapter(favouritesDay1, eventListener);
            if(adapter == null)
                Log.i("RV", "displayEvents: null Adapter ");
            recyclerViewDay1.setAdapter(adapter);
            recyclerViewDay1.setNestedScrollingEnabled(false);
            recyclerViewDay1.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        }
        if(favouritesDay2.isEmpty()){
            recyclerViewDay2.setVisibility(View.GONE);
            ((View)recyclerViewDay2.getParent()).setVisibility(View.GONE);
            noEventsDay2.setVisibility(View.VISIBLE);
            ((View)noEventsDay2.getParent()).setVisibility(View.VISIBLE);
        }else{
            recyclerViewDay2.setAdapter(new FavouritesEventsAdapter(favouritesDay2, eventListener));
            recyclerViewDay2.setNestedScrollingEnabled(false);
            recyclerViewDay2.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        }
        if(favouritesDay3.isEmpty()){
            recyclerViewDay3.setVisibility(View.GONE);
            ((View)recyclerViewDay3.getParent()).setVisibility(View.GONE);
            noEventsDay3.setVisibility(View.VISIBLE);
            ((View)noEventsDay3.getParent()).setVisibility(View.VISIBLE);

        }else{
            recyclerViewDay3.setAdapter(new FavouritesEventsAdapter(favouritesDay3, eventListener));
            recyclerViewDay3.setNestedScrollingEnabled(false);
            recyclerViewDay3.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        }
        if(favouritesDay4.isEmpty()){
            recyclerViewDay4.setVisibility(View.GONE);
            ((View)recyclerViewDay4.getParent()).setVisibility(View.GONE);
            noEventsDay4.setVisibility(View.VISIBLE);
            ((View)noEventsDay4.getParent()).setVisibility(View.VISIBLE);

        }else{
            recyclerViewDay4.setAdapter(new FavouritesEventsAdapter(favouritesDay4, eventListener));
            recyclerViewDay4.setNestedScrollingEnabled(false);
            recyclerViewDay4.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }
    private void displayBottomSheet(FavouritesModel event){
        View view = View.inflate(getContext(), R.layout.activity_event_dialogue, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());

        String eventID = event.getId();

        EventDetailsModel schedule = realm.where(EventDetailsModel.class).equalTo("eventID",eventID).findFirst();


        TextView eventName = (TextView)view.findViewById(R.id.event_name);
        eventName.setText(event.getEventName());

        TextView eventRound = (TextView)view.findViewById(R.id.event_round);
        eventRound.setText(event.getRound());

        TextView eventDate = (TextView)view.findViewById(R.id.event_date);
        eventDate.setText(event.getDate());

        TextView eventTime = (TextView)view.findViewById(R.id.event_time);
        eventTime.setText(event.getStartTime() + " - " + event.getEndTime());

        TextView eventVenue = (TextView)view.findViewById(R.id.event_venue);
        eventVenue.setText(event.getVenue());

        TextView eventTeamSize = (TextView)view.findViewById(R.id.event_team_size);
        eventTeamSize.setText(schedule.getMaxTeamSize());

        TextView eventCategory = (TextView)view.findViewById(R.id.event_category);
        eventCategory.setText(event.getCatName());

        TextView eventContact = (TextView)view.findViewById(R.id.event_contact);
        eventContact.setText(schedule.getContactName() + " ( " + schedule.getContactNo() + " )");

        TextView eventDescription = (TextView)view.findViewById(R.id.event_description);
        eventDescription.setText(schedule.getDescription());

        dialog.setContentView(view);
        dialog.show();

    }
}
