package in.tt.tt17.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.tt.tt17.R;

/**
 * Created by bennyhawk on 20/6/17.
 */

public class FilterVenueAdapter extends RecyclerView.Adapter<FilterVenueAdapter.FilterVenueViewHolder> {

    private List<String> mVenueNames = new ArrayList<>();
    private  Context mContext;
    private TextView mVenue;
    String currentlySelectedVenue = "";
    ArrayList<CardView> allViews = new ArrayList<>();

    public FilterVenueAdapter(List<String> venueNames, Context context,TextView venue){
        this.mVenueNames=venueNames;
        this.mContext=context;
        this.mVenue=venue;
        currentlySelectedVenue = venue.getText().toString();
    }
    @Override
    public FilterVenueAdapter.FilterVenueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterVenueViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_filter_venue,parent,false));
    }
    @Override
    public void onBindViewHolder(FilterVenueAdapter.FilterVenueViewHolder holder, int position) {

            holder.textView1.setText(mVenueNames.get(position));
            holder.cardView1.setId(position);
            TextView tempView = (TextView) holder.cardView1.getChildAt(0);

            if(currentlySelectedVenue.equals(holder.textView1.getText().toString())){
                tempView.setTextColor(Color.WHITE);
                holder.cardView1.setCardBackgroundColor(Color.parseColor("#18BADE"));
            }else{
                tempView.setTextColor(Color.parseColor("#ffffff"));
                holder.cardView1.setCardBackgroundColor(Color.parseColor("#424242"));
            }

            allViews.add(position,holder.cardView1);
    }
    @Override
    public int getItemCount() {
        return (mVenueNames.size());
    }

    public class FilterVenueViewHolder extends RecyclerView.ViewHolder {
        CardView cardView1;
        TextView textView1;
        public FilterVenueViewHolder(final View itemView) {
            super(itemView);
            cardView1 = (CardView) itemView.findViewById(R.id.cardView);
            textView1 = (TextView) itemView.findViewById(R.id.filter_venue_text_one);
            cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!currentlySelectedVenue.equals("")){
                    for (CardView tempCard : allViews){
                        TextView tempView = (TextView) tempCard.getChildAt(0);
                        if (tempView.getText().toString().equals(currentlySelectedVenue)){
                            tempView.setTextColor(Color.parseColor("#ffffff"));
                            tempCard.setCardBackgroundColor(Color.parseColor("#424242"));
                        }
                    }
                }
                cardView1.setCardBackgroundColor(Color.parseColor("#18BADE"));
                TextView tempView = (TextView) cardView1.getChildAt(0);
                tempView.setTextColor(Color.WHITE);
                currentlySelectedVenue = textView1.getText().toString();
                mVenue.setText(tempView.getText());
                }
            });
        }
    }
}