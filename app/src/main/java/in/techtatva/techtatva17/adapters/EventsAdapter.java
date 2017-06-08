package in.techtatva.techtatva17.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.activities.EventActivity;
import in.techtatva.techtatva17.models.events.EventDetailsModel;
import in.techtatva.techtatva17.models.events.EventsListModel;

/**
 * Created by skvrahul on 30/5/17.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private EventsListModel eventList;
    private final EventClickListener eventListener;
    private final FavouriteClickListener favouriteListener;


    //Interfaces to ClickListener of the item and of the Favourite Icon in the item
    public interface EventClickListener {
        void onItemClick(EventDetailsModel event);
    }
    public interface FavouriteClickListener {
        void onItemClick(EventDetailsModel event);
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
        public void onBind(final EventDetailsModel event, final EventClickListener eventListener, final FavouriteClickListener favouriteListener){
            eventName.setText(event.getEventName());

            //TODO: Change this once the Data Models are updated to include these fields
            eventTime.setText("12:00AM- 12:00PM");
            eventIcon.setImageResource(R.drawable.ic_sample_image_24dp);
            eventVenue.setText("NLH 404");
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

        }
    }
    public EventsAdapter(EventsListModel events,EventClickListener eventListener, FavouriteClickListener favouriteListener){
        this.eventList = events;
        this.eventListener = eventListener;
        this.favouriteListener = favouriteListener;
    }
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        EventDetailsModel event = eventList.getEvents().get(position);
        holder.onBind(event,eventListener, favouriteListener);
    }

    @Override
    public int getItemCount() {
        return eventList.getEvents().size();
    }
}
