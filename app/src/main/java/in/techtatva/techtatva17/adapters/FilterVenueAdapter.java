package in.techtatva.techtatva17.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.techtatva.techtatva17.R;

/**
 * Created by bennyhawk on 20/6/17.
 */

public class FilterVenueAdapter extends RecyclerView.Adapter<FilterVenueAdapter.FilterVenueViewHolder> {

    private List<String> mVenueNames = new ArrayList<>();
    private  Context mContext;
    private TextView mVenue;

    int currentlySelectedCard=-1;
    View currentlySelectedText;

    ArrayList<CardView> allViews = new ArrayList<>();

    public FilterVenueAdapter(List<String> venueNames, Context context,TextView venue){
        this.mVenueNames=venueNames;
        this.mContext=context;
        this.mVenue=venue;

    }

    @Override
    public FilterVenueAdapter.FilterVenueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterVenueViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_filter_venue,parent,false));
    }

    @Override
    public void onBindViewHolder(FilterVenueAdapter.FilterVenueViewHolder holder, int position) {


        if ((position*2)<mVenueNames.size()){
            holder.textView1.setText(mVenueNames.get(position*2));
            holder.cardView1.setId(position*2);
            allViews.add(position*2,holder.cardView1);
        }
        else{
            holder.cardView1.setVisibility(View.GONE);
        }
        if ((position*2)+1<mVenueNames.size()){
            holder.textView2.setText(mVenueNames.get(position*2 +1));
            holder.cardView2.setId((position*2) +1);
            allViews.add((position*2) +1,holder.cardView2);
        }
        else{
            holder.cardView2.setVisibility(View.GONE);
        }



        /*
        if ((position*3)+2<mVenueNames.size()){

            //holder.textView3.setText(mVenueNames.get(position*3 + 2));
            //holder.cardView3.setId(position*3 + 2);
            //allViews.add((position*3) +2,holder.cardView3);
        }
        else{
            holder.cardView3.setVisibility(View.GONE);
        }*/

    }

    @Override
    public int getItemCount() {
        return (int)(mVenueNames.size()/2 +1 );
    }

    public class FilterVenueViewHolder extends RecyclerView.ViewHolder {
        CardView cardView1;
        CardView cardView2;


        TextView textView1;
        TextView textView2;


        public FilterVenueViewHolder(final View itemView) {

            super(itemView);




             cardView1 = (CardView) itemView.findViewById(R.id.cardView);
             cardView2 = (CardView) itemView.findViewById(R.id.cardView2);


             textView1 = (TextView) itemView.findViewById(R.id.filter_venue_text_one);
             textView2 = (TextView) itemView.findViewById(R.id.filter_venue_text_two);



            cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentlySelectedCard != -1){
                        CardView tempCard = allViews.get(currentlySelectedCard);
                        Log.d("TAG",String.valueOf(currentlySelectedCard));
                        TextView tempView = (TextView) tempCard.getChildAt(0);
                        tempView.setTextColor(Color.parseColor("#8a000000"));

                        tempCard.setCardBackgroundColor(Color.WHITE);


                    }
                    cardView1.setCardBackgroundColor(Color.parseColor("#3f51b5"));
                    TextView tempView = (TextView) cardView1.getChildAt(0);
                    tempView.setTextColor(Color.WHITE);
                    currentlySelectedCard=cardView1.getId();
                    mVenue.setText(tempView.getText());
                    Log.d("TAG",String.valueOf(currentlySelectedCard));




                }
            });
            cardView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(currentlySelectedCard != -1){
                        CardView tempCard = allViews.get(currentlySelectedCard);
                        Log.d("TAG",String.valueOf(currentlySelectedCard));
                        TextView tempView = (TextView) tempCard.getChildAt(0);
                        tempView.setTextColor(Color.parseColor("#8a000000"));

                        tempCard.setCardBackgroundColor(Color.WHITE);

                    }
                    cardView2.setCardBackgroundColor(Color.parseColor("#3f51b5"));
                    TextView tempView = (TextView) cardView2.getChildAt(0);
                    tempView.setTextColor(Color.WHITE);
                    currentlySelectedCard=cardView2.getId();
                    mVenue.setText(tempView.getText());
                    Log.d("TAG",String.valueOf(currentlySelectedCard));

                }
            });





        }
    }
}
