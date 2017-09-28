package in.tt.tt17.adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.tt.tt17.R;
import in.tt.tt17.models.events.EventDetailsModel;
import in.tt.tt17.models.events.EventModel;
import in.tt.tt17.models.events.ScheduleModel;
import in.tt.tt17.models.favourites.FavouritesModel;
import in.tt.tt17.receivers.NotificationReceiver;
import in.tt.tt17.resources.IconCollection;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by skvrahul on 30/5/17.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {
    String TAG = "EventsAdapter";
    private final int EVENT_DAY_ZERO = 03;
    private final int EVENT_MONTH = Calendar.OCTOBER;
    private PendingIntent pendingIntent1 = null;
    private PendingIntent pendingIntent2 = null;
    private List<EventModel> eventList;
    private final EventClickListener eventListener;
    private final FavouriteClickListener favouriteListener;
    private final EventLongPressListener eventLongPressListener;
    private Realm realm = Realm.getDefaultInstance();
    private RealmResults<FavouritesModel> favouritesRealm = realm.where(FavouritesModel.class).findAll();
    private List<FavouritesModel> favourites = realm.copyFromRealm(favouritesRealm);
    private Activity activity;

    public interface EventClickListener {
        void onItemClick(ScheduleModel event, View view);
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
        public void onBind(final EventModel events, final EventClickListener eventListener,final EventLongPressListener eventLongPressListener, final FavouriteClickListener favouriteListener){
            //Individual OnClickListeners for the Favourite Icon and the entire Item
            final ScheduleModel event = realm.where(ScheduleModel.class).equalTo("eventID",events.getEventId()).equalTo("startTime",events.getStartTime()).equalTo("day",events.getDay()).findFirst();
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
                    eventListener.onItemClick(event,v);
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
    public EventsAdapter(Activity activity, List<EventModel> events, EventClickListener eventListener, EventLongPressListener eventLongPressListener, FavouriteClickListener favouriteListener){
        this.eventList = events;
        this.eventListener = eventListener;
        this.favouriteListener = favouriteListener;
        this.eventLongPressListener=eventLongPressListener;
        this.activity = activity;
    }
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        EventModel event = eventList.get(position);
        if(event.getEventName().contains("V")){
            Log.d("T",event.getEventName());
            Log.d("T",event.getEventId());
            Log.d("T",event.getDay());
            Log.d("T"," ");}
        IconCollection icons = new IconCollection();
        holder.eventIcon.setImageResource(icons.getIconResource(activity, event.getCatName()));
        holder.onBind(event,eventListener, eventLongPressListener,favouriteListener );
    }
    @Override
    public int getItemCount() {
        return eventList.size();
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
        addNotification(eventSchedule);
        favourites.add(favourite);
    }
    private void removeFavourite(ScheduleModel eventSchedule){
        realm.beginTransaction();
        realm.where(FavouritesModel.class).equalTo("id",eventSchedule.getEventID()).equalTo("day",eventSchedule.getDay()).findAll().deleteAllFromRealm();
        realm.commitTransaction();

        for(int i=0;i<favourites.size();i++){
            //Removing corresponding FavouritesModel from favourites
            FavouritesModel favourite = favourites.get(i);
            if((favourite.getId().equals(eventSchedule.getEventID()))&&(favourite.getDay().equals(eventSchedule.getDay()))){
                favourites.remove(favourite);
            }
        }
        removeNotification(eventSchedule);
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
    private void addNotification(ScheduleModel event){
        Intent intent = new Intent(activity, NotificationReceiver.class);
        intent.putExtra("eventName", event.getEventName());
        intent.putExtra("startTime", event.getStartTime());
        intent.putExtra("eventVenue", event.getVenue());
        intent.putExtra("eventID", event.getEventID());
        intent.putExtra("catName", event.getCatName());
        Log.i(TAG, "addNotification: "+event.getStartTime());
        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        //Request Codes
        int RC1 = Integer.parseInt(event.getCatID()+event.getEventID()+"0");
        int RC2 = Integer.parseInt(event.getCatID()+event.getEventID()+"1");
        pendingIntent1 = PendingIntent.getBroadcast(activity, RC1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent2 = PendingIntent.getBroadcast(activity, RC2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);
        Date d = null;
        try {
            d = sdf.parse(event.getStartTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        int eventDate = EVENT_DAY_ZERO + Integer.parseInt(event.getDay());   //event dates start from 04th October
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(d);
        calendar1.set(Calendar.MONTH,EVENT_MONTH);
        calendar1.set(Calendar.YEAR, 2017);
        calendar1.set(Calendar.DATE, eventDate);
        calendar1.set(Calendar.SECOND, 0);
        long eventTimeInMillis = calendar1.getTimeInMillis();
        calendar1.set(Calendar.HOUR_OF_DAY, calendar1.get(Calendar.HOUR_OF_DAY)-1);

        Calendar calendar2 = Calendar.getInstance();
        Log.d("Calendar 1", calendar1.getTimeInMillis()+"");
        Log.d("Calendar 2", calendar2.getTimeInMillis()+"");

        if(calendar2.getTimeInMillis() <= eventTimeInMillis)
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);

        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.SECOND, 0);
        calendar3.set(Calendar.MINUTE, 30);
        calendar3.set(Calendar.HOUR, 8);
        calendar3.set(Calendar.AM_PM, Calendar.AM);
        calendar3.set(Calendar.MONTH, Calendar.SEPTEMBER);
        calendar3.set(Calendar.YEAR, 2017);
        calendar3.set(Calendar.DATE, eventDate);
        Log.d("Calendar 3", calendar3.getTimeInMillis()+"");
        if (calendar2.getTimeInMillis() < calendar3.getTimeInMillis()){
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent2);

            Log.d("Alarm", "set for "+calendar3.toString());
        }
    }
    private void removeNotification(ScheduleModel event){
        Intent intent = new Intent(activity, NotificationReceiver.class);
        intent.putExtra("eventName", event.getEventName());
        intent.putExtra("startTime", event.getStartTime());
        intent.putExtra("eventVenue", event.getVenue());
        intent.putExtra("eventID", event.getEventID());

        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        //Request Codes
        int RC1 = Integer.parseInt(event.getCatID()+event.getEventID()+"0");
        int RC2 = Integer.parseInt(event.getCatID()+event.getEventID()+"1");
        pendingIntent1 = PendingIntent.getBroadcast(activity, RC1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent2 = PendingIntent.getBroadcast(activity, RC2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent1);
        alarmManager.cancel(pendingIntent2);
    }
}