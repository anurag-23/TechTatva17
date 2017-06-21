package in.techtatva.techtatva17.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.models.favourites.FavouritesModel;

/**
 * Created by skvrahul on 14/6/17.
 */

public class FavouritesEventsAdapter extends RecyclerView.Adapter<FavouritesEventsAdapter.EventViewHolder> {

    private List<FavouritesModel> favourites;
    private final EventClickListener eventListener;

    public interface EventClickListener {
        void onItemClick(FavouritesModel event);
    }

    public FavouritesEventsAdapter(List<FavouritesModel> favourites, EventClickListener eventListener) {
        this.favourites = favourites;
        this.eventListener = eventListener;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourite_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        FavouritesModel event = favourites.get(position);
        holder.onBind(event);
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        public ImageView eventLogo;
        public TextView eventRound;
        public TextView eventName;
        public TextView eventTime;
        public RelativeLayout eventItem;

        public EventViewHolder(View view) {
            super(view);
            eventLogo = (ImageView) view.findViewById(R.id.fav_event_logo_image_view);
            eventRound = (TextView) view.findViewById(R.id.fav_event_round_text_view);
            eventName = (TextView) view.findViewById(R.id.fav_event_name_text_view);
            eventTime = (TextView) view.findViewById(R.id.fav_event_time_text_view);
            eventItem = (RelativeLayout)view.findViewById(R.id.fav_event_item);

        }
        public void onBind(final FavouritesModel event) {

            eventName.setText(event.getEventName());
            eventRound.setText(event.getRound());
            eventTime.setText(event.getStartTime()+" - "+ event.getEndTime());
            eventItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onItemClick(event);
                }
            });

        }
    }
}

