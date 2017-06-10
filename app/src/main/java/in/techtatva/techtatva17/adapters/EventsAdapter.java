package in.techtatva.techtatva17.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.models.events.EventDetailsModel;
import in.techtatva.techtatva17.models.events.EventsListModel;
import in.techtatva.techtatva17.models.events.ScheduleListModel;
import in.techtatva.techtatva17.models.events.ScheduleModel;

/**
 * Created by skvrahul on 30/5/17.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private ScheduleListModel eventList;
    private final EventClickListener eventListener;
    private final FavouriteClickListener favouriteListener;
    private final EventLongPressListener eventLongPressListener;


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
        public ImageView eventIcon, fav;
        public RelativeLayout eventItem;
        public EventViewHolder(View view){
            super(view);
            eventIcon = (ImageView)view.findViewById(R.id.event_logo_image_view);
            fav = (ImageView)view.findViewById(R.id.event_fav_ico);
            eventName = (TextView)view.findViewById(R.id.event_name_text_view);
            eventVenue = (TextView)view.findViewById(R.id.event_venue_text_view);
            eventTime = (TextView)view.findViewById(R.id.event_time_text_view);
            eventItem = (RelativeLayout)view.findViewById(R.id.event_item_relative_layout);
        }
        public void onBind(final ScheduleModel event, final EventClickListener eventListener,final EventLongPressListener eventLongPressListener, final FavouriteClickListener favouriteListener){
            eventName.setText(event.getEventName());


            eventTime.setText(event.getStartTime() + " - " + event.getEndTime());
            eventIcon.setImageResource(R.drawable.ic_sample_image_24dp);
            eventVenue.setText(event.getVenue());
            //Individual OnClickListeners for the Favourite Icon and the entire Item
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favouriteListener.onItemClick(event);
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
}
