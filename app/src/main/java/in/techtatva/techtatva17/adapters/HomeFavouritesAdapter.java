package in.techtatva.techtatva17.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.models.events.EventDetailsModel;
import in.techtatva.techtatva17.models.favourites.FavouritesModel;
import io.realm.Realm;

/**
 * Created by skvrahul on 14/6/17.
 */

public class HomeFavouritesAdapter extends RecyclerView.Adapter<HomeFavouritesAdapter.EventViewHolder> {

    private List<FavouritesModel> favourites;
    private final EventClickListener eventListener;
    private Context context;
    private Realm mDatabase = Realm.getDefaultInstance();

    public interface EventClickListener {
        void onItemClick(FavouritesModel event);
    }

    public HomeFavouritesAdapter(List<FavouritesModel> favourites, EventClickListener eventListener) {
        this.favourites = favourites;
        this.eventListener = eventListener;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
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
            eventTime.setVisibility(View.GONE);
            eventItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(eventListener!=null){
                        eventListener.onItemClick(event);
                    }
                    displayBottomSheet(event);
                }
            });

        }
        private void displayBottomSheet(final FavouritesModel event){
            final View view = View.inflate(context, R.layout.activity_event_dialogue, null);
            final BottomSheetDialog dialog = new BottomSheetDialog(context);
            Log.i("TT17", "displayBottomSheet: NEW!");
            final String eventID = event.getId();

            EventDetailsModel schedule = mDatabase.where(EventDetailsModel.class).equalTo("eventID",eventID).findFirst();


            final TextView eventName = (TextView)view.findViewById(R.id.event_name);
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

            TextView eventContactName = (TextView) view.findViewById(R.id.event_contact_name);
            eventContactName.setText(event.getContactName() + " : ");

            TextView eventContact = (TextView) view.findViewById(R.id.event_contact);
            eventContact.setText(  event.getContactNumber());
            eventContact.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

            eventContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + event.getContactNumber()));
                    context.startActivity(intent);
                }
            });

            TextView eventDescription = (TextView)view.findViewById(R.id.event_description);
            eventDescription.setText(schedule.getDescription());

            ImageView deleteIcon = (ImageView)view.findViewById(R.id.event_delete_icon);
            deleteIcon.setVisibility(View.GONE);
            dialog.setContentView(view);
            dialog.show();
        }

    }
}
