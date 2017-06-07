package in.techtatva.techtatva17.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.activities.EventActivity;
import in.techtatva.techtatva17.models.events.EventModel;

/**
 * Created by sapta on 6/6/2017.
 */

public class CategoryEventsAdapter extends RecyclerView.Adapter<CategoryEventsAdapter.CategoryEventsViewHolder> {

    private List<EventModel> eventsList;
    private Activity activity;

    public CategoryEventsAdapter(List<EventModel> eventsList, Activity activity){
        this.eventsList=eventsList;
        this.activity=activity;
    }

    @Override
    public CategoryEventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryEventsViewHolder(LayoutInflater.from(activity).inflate(R.layout.category_event, parent, false));
    }

    @Override
    public void onBindViewHolder(final CategoryEventsViewHolder holder, int position) {
        EventModel event = eventsList.get(position);

        holder.eventName.setText(event.getEventName());
        holder.eventTime.setText(event.getStartTime());


        holder.eventRound.setVisibility(View.VISIBLE);

        if (event.getRound() != null && !event.getRound().equals("-") && !event.getRound().equals("")){
            if (event.getRound().toLowerCase().charAt(0) == 'r')
                holder.eventRound.setText(event.getRound().toUpperCase());
            else
                holder.eventRound.setText("R"+event.getRound().toUpperCase().charAt(0));
        }
        else{
            holder.eventRound.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class CategoryEventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView eventLogo;
        TextView eventName;
        TextView eventTime;
        FrameLayout logoFrame;
        TextView eventRound;

        public CategoryEventsViewHolder(View itemView) {
            super(itemView);

            eventLogo = (ImageView) itemView.findViewById(R.id.cat_event_logo_image_view);
            eventName = (TextView) itemView.findViewById(R.id.cat_event_name_text_view);
            eventTime = (TextView) itemView.findViewById(R.id.cat_event_time_text_view);
            logoFrame = (FrameLayout) itemView.findViewById(R.id.fav_event_logo_frame);
            eventRound = (TextView) itemView.findViewById(R.id.cat_event_round_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, EventActivity.class);
            activity.startActivity(intent);
        }
    }
}
