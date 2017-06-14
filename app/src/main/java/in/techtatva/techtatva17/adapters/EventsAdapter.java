package in.techtatva.techtatva17.adapters;

import android.app.usage.UsageEvents;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.models.events.EventDetailsModel;
import in.techtatva.techtatva17.models.events.EventsListModel;
import in.techtatva.techtatva17.models.events.ScheduleListModel;
import in.techtatva.techtatva17.models.events.ScheduleModel;
import in.techtatva.techtatva17.models.favourites.FavouritesModel;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by skvrahul on 30/5/17.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private ScheduleListModel eventList;
    private final EventClickListener eventListener;
    private final FavouriteClickListener favouriteListener;
    private final EventLongPressListener eventLongPressListener;
    private Realm realm = Realm.getDefaultInstance();
    private RealmResults<FavouritesModel> favouritesRealm = realm.where(FavouritesModel.class).findAll();
    private List<FavouritesModel> favourites = realm.copyFromRealm(favouritesRealm);

//    private List<FavouritesModel> favouritesModelList = new ArrayList<>();

    //Interfaces to ClickListener of the item and of the Favourite Icon in the item
    public interface EventClickListener {
        void onItemClick(ScheduleModel event);
    }
    public interface FavouriteClickListener {
        void onItemClick(ScheduleModel event);
    }

    public interface EventLongPressListener{
        void onItemLongPress(ScheduleModel event);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder{
        public TextView eventName, eventVenue, eventTime;
        public ImageView eventIcon, favIcon;
        public RelativeLayout eventItem;
        public EventViewHolder(View view){
            super(view);
            eventIcon = (ImageView)view.findViewById(R.id.event_logo_image_view);
            favIcon = (ImageView)view.findViewById(R.id.event_fav_ico);
            eventName = (TextView)view.findViewById(R.id.event_name_text_view);
            eventVenue = (TextView)view.findViewById(R.id.event_venue_text_view);
            eventTime = (TextView)view.findViewById(R.id.event_time_text_view);
            eventItem = (RelativeLayout)view.findViewById(R.id.event_item_relative_layout);
        }
        public void onBind(final ScheduleModel event, final EventClickListener eventListener,final EventLongPressListener eventLongPressListener, final FavouriteClickListener favouriteListener){
            //Individual OnClickListeners for the Favourite Icon and the entire Item
            favIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favouriteListener.onItemClick(event);
                    if(favIcon.getTag().equals("deselected")) {
                        favIcon.setImageResource(R.drawable.ic_fav_selected);
                        favIcon.setTag("selected");
                        addFavourite(event);
                        Snackbar.make(v, event.getEventName()+" Added to Favourites", Snackbar.LENGTH_LONG).show();
                    }else{
                        favIcon.setImageResource(R.drawable.ic_fav_deselected);
                        favIcon.setTag("deselected");
                        removeFavourite(event);
                        Snackbar.make(v, event.getEventName()+" removed from Favourites", Snackbar.LENGTH_LONG).show();
                    }

                }
            });
            eventItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onItemClick(event);

                }
            });
            eventItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    eventLongPressListener.onItemLongPress(event);
                    return false;
                }
            });
            eventName.setText(event.getEventName());
            eventTime.setText(event.getStartTime() + " - " + event.getEndTime());
            eventIcon.setImageResource(R.drawable.ic_sample_image_24dp);
            eventVenue.setText(event.getVenue());
            if(favouritesContainsEvent(event)){
                favIcon.setImageResource(R.drawable.ic_fav_selected);
                favIcon.setTag("selected");
            }else{
                favIcon.setImageResource(R.drawable.ic_fav_deselected);
                favIcon.setTag("deselected");
            }

        }
    }


    public EventsAdapter(ScheduleListModel events,EventClickListener eventListener,EventLongPressListener eventLongPressListener, FavouriteClickListener favouriteListener){
        this.eventList = events;
        this.eventListener = eventListener;
        this.favouriteListener = favouriteListener;
        this.eventLongPressListener=eventLongPressListener;
    }


    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);

        return new EventViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        ScheduleModel event = eventList.getData().get(position);
        holder.onBind(event,eventListener, eventLongPressListener,favouriteListener );
    }

    @Override
    public int getItemCount() {
        return eventList.getData().size();
    }

    private void addFavourite(ScheduleModel eventSchedule){


        FavouritesModel favourite = new FavouritesModel();

        //Get Corresponding EventDetailsModel from Realm
        EventDetailsModel eventDetails = realm.where(EventDetailsModel.class).equalTo("eventID",eventSchedule.getEventID()).findFirst();

        //Create and Set Values for FavouritesModel
        favourite.setId(eventSchedule.getEventID());
        favourite.setCatID(eventSchedule.getCatID());
        favourite.setEventName(eventSchedule.getEventName());
        favourite.setRound(eventSchedule.getRound());
        favourite.setVenue(eventSchedule.getVenue());
        favourite.setDate(eventSchedule.getDate());
        favourite.setDay(eventSchedule.getDay());
        favourite.setStartTime(eventSchedule.getStartTime());
        favourite.setEndTime(eventSchedule.getEndTime());

        favourite.setParticipants(eventDetails.getMaxTeamSize());
        favourite.setContactName(eventDetails.getContactName());
        favourite.setContactNumber(eventDetails.getContactNo());
        favourite.setCatName(eventDetails.getCatName());
        favourite.setDescription(eventDetails.getDescription());
        //Commit to Realm
        realm.beginTransaction();
        realm.copyToRealm(favourite);
        realm.commitTransaction();

        favourites.add(favourite);

    }
    private void removeFavourite(ScheduleModel eventSchedule){
        realm.beginTransaction();
        realm.where(FavouritesModel.class).equalTo("id",eventSchedule.getEventID()).equalTo("day",eventSchedule.getDay()).findAll().deleteAllFromRealm();
        realm.commitTransaction();

        for(FavouritesModel favourite : favourites){
            //Removing corresponding FavouritesModel from favourites
            if((favourite.getId().equals(eventSchedule.getEventID()))&&(favourite.getDay().equals(eventSchedule.getDay()))){
                favourites.remove(favourite);

            }
        }
    }
    private boolean favouritesContainsEvent(ScheduleModel eventSchedule){
        for(FavouritesModel favourite : favourites){
            //Checking if Corresponding Event exists
            if((favourite.getId().equals(eventSchedule.getEventID()))&&(favourite.getDay().equals(eventSchedule.getDay()))){
                return true;

            }
        }
        return false;
    }
}
