package in.tt.tt17.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.tt.tt17.R;
import in.tt.tt17.models.result.EventResultModel;
import in.tt.tt17.resources.IconCollection;

/**
 * Created by skvrahul on 15/7/17.
 */
public class HomeResultsAdapter extends RecyclerView.Adapter<HomeResultsAdapter. HomeViewHolder> {
    String TAG = "HomeResultsAdapter";
    private List<EventResultModel> resultsList;
    private Context context;
    Activity activity;

    public HomeResultsAdapter(List<EventResultModel> resultsList, Activity activity) {
        this.resultsList=resultsList;
        this.activity = activity;
    }
    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_results, parent, false);
        context = parent.getContext();
        return new  HomeViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder( HomeViewHolder holder, int position) {
        EventResultModel result = resultsList.get(position);
        holder.onBind(result);
        IconCollection icons = new IconCollection();
        holder.resultsLogo.setImageResource(icons.getIconResource(activity, result.eventCategory));
    }
    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public class  HomeViewHolder extends RecyclerView.ViewHolder{
        public ImageView resultsLogo;
        public TextView resultsName;
        public TextView resultsRound;
        public FrameLayout resultsItem;
        public  HomeViewHolder(View view) {
            super(view);
            initializeViews(view);
        }

        public void onBind(final EventResultModel result) {
            if(resultsItem!=null) {
                resultsName.setText(result.eventName);
                resultsRound.setText("Round: "+result.eventRound);
                resultsItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "onClick: Item Clicked!");
                        displayBottomSheet(resultsList.get(getAdapterPosition()));
                    }
                });
            }
        }
        public void initializeViews(View view){
            resultsLogo = (ImageView) view.findViewById(R.id.home_results_logo_image_view);
            resultsName = (TextView) view.findViewById(R.id.home_results_name_text_view);
            resultsRound = (TextView) view.findViewById(R.id.home_results_round_text_view);
            resultsItem = (FrameLayout) view.findViewById(R.id.home_results_frame);
            Log.i(TAG, "initializeViews:"+resultsItem);
        }
        public void displayBottomSheet(EventResultModel result){
            View bottomSheetView = View.inflate(context, R.layout.dialog_results, null);
            final BottomSheetDialog dialog = new BottomSheetDialog(context);
            dialog.setContentView(bottomSheetView);

            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            TextView eventName = (TextView)bottomSheetView.findViewById(R.id.result_dialog_event_name_text_view);
            eventName.setText(result.eventName);

            TextView eventRound = (TextView)bottomSheetView.findViewById(R.id.result_dialog_round_text_view);
            eventRound.setText(result.eventRound);

            RecyclerView teamsRecyclerView = (RecyclerView)bottomSheetView.findViewById(R.id.result_dialog_teams_recycler_view);
            teamsRecyclerView.setAdapter(new QualifiedTeamsAdapter(result.eventResultsList, context));
            teamsRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));

            dialog.show();
        }
    }
}