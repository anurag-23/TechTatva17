package in.techtatva.techtatva17.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.models.events.EventModel;

/**
 * Created by sapta on 6/6/2017.
 */

public class CategoryEventsAdapter extends RecyclerView.Adapter<CategoryEventsAdapter.CategoryEventsViewHolder> {

    private List<EventModel> eventsList;

    private Activity activity;
    private Context context;
    EventModel event;


    public CategoryEventsAdapter(List<EventModel> eventsList, Activity activity, Context context) {
        this.eventsList = eventsList;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public CategoryEventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryEventsViewHolder(LayoutInflater.from(activity).inflate(R.layout.category_event, parent, false));
    }

    @Override
    public void onBindViewHolder(final CategoryEventsViewHolder holder, int position) {
        event = eventsList.get(position);

        holder.eventName.setText(event.getEventName());
        holder.eventTime.setText(event.getStartTime());


        holder.eventRound.setVisibility(View.VISIBLE);

        if (event.getRound() != null && !event.getRound().equals("-") && !event.getRound().equals("")) {
            if (event.getRound().toLowerCase().charAt(0) == 'r')
                holder.eventRound.setText(event.getRound().toUpperCase());
            else
                holder.eventRound.setText("R" + event.getRound().toUpperCase().charAt(0));
        } else {
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

            final EventModel event = eventsList.get(getLayoutPosition());

            final Context context = view.getContext();
            view = View.inflate(context, R.layout.activity_event_dialogue, null);

            final BottomSheetDialog dialog = new BottomSheetDialog(context);
            

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
            eventTeamSize.setText(event.getEventMaxTeamNumber());

            TextView eventCategory = (TextView) view.findViewById(R.id.event_category);
            eventCategory.setText(event.getCatName());

            TextView eventContactName = (TextView) view.findViewById(R.id.event_contact_name);
            eventContactName.setText(event.getContactName() + " : ");

            TextView eventContact = (TextView) view.findViewById(R.id.event_contact);
            eventContact.setText(  event.getContactNumber());
            eventContact.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

            eventContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + event.getContactNumber()));


                    activity.startActivity(intent);
                }
            });

            TextView eventDescription = (TextView)view.findViewById(R.id.event_description);
            eventDescription.setText(event.getDescription());

            ImageView deleteIcon = (ImageView)view.findViewById(R.id.event_delete_icon);
            deleteIcon.setVisibility(View.GONE);

            dialog.setContentView(view);
            dialog.show();


        }
    }


}
