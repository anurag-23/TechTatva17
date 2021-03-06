package in.tt.tt17.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.tt.tt17.R;
import in.tt.tt17.adapters.CategoryEventsAdapter;
import in.tt.tt17.models.events.EventDetailsModel;
import in.tt.tt17.models.events.EventModel;
import in.tt.tt17.models.events.ScheduleModel;
import io.realm.Realm;
import io.realm.RealmResults;

public class CategoryActivity extends AppCompatActivity {

    private String catName;
    private String catID;
    private String catDesc;
    private Realm mDatabase;
    private TextView noEventsDay1;
    private TextView noEventsDay2;
    private TextView noEventsDay3;
    private TextView noEventsDay4;
    private TextView catNameTextView;
    private TextView catDescTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        catName = getIntent().getStringExtra("catName");
        catID = getIntent().getStringExtra("catID");
        catDesc = getIntent().getStringExtra("catDesc");
        if (catName == null) catName = "";
        if (catID == null) catID = "";
        if (catDesc == null) catDesc = "";

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.parseColor("#0d0d0d"));
            getWindow().setNavigationBarColor(Color.parseColor("#0d0d0d"));

        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.category_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(catName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mDatabase = Realm.getDefaultInstance();
        displayEvents();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category_activity, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.about_category:
                View view = View.inflate(this, R.layout.dialog_about_category, null);
                final BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(view);
                catNameTextView = (TextView)view.findViewById(R.id.category_about_name);
                catDescTextView = (TextView)view.findViewById(R.id.category_about_description);
                catNameTextView.setText(catName);
                catDescTextView.setText(catDesc);

                dialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void displayEvents(){

        List<EventModel>day1List=new ArrayList<>();
        List<EventModel>day2List=new ArrayList<>();
        List<EventModel>day3List=new ArrayList<>();
        List<EventModel>day4List=new ArrayList<>();

        noEventsDay1 = (TextView)findViewById(R.id.cat_day_1_no_events);
        noEventsDay2 = (TextView)findViewById(R.id.cat_day_2_no_events);
        noEventsDay3 = (TextView)findViewById(R.id.cat_day_3_no_events);
        noEventsDay4 = (TextView)findViewById(R.id.cat_day_4_no_events);

        if (mDatabase == null)
            return;

        RealmResults<ScheduleModel> scheduleResults = mDatabase.where(ScheduleModel.class).equalTo("catID", catID).findAllSorted("startTime");

        for (ScheduleModel schedule : scheduleResults){
            EventDetailsModel eventDetails = mDatabase.where(EventDetailsModel.class).equalTo("eventID", schedule.getEventID()).findFirst();

            EventModel event = new EventModel(eventDetails, schedule);
            switch(event.getDay()){
                case "1": day1List.add(event);
                    break;
                case "2": day2List.add(event);
                    break;
                case "3": day3List.add(event);
                    break;
                case "4": day4List.add(event);
                    break;
            }
        }
        eventSort(day1List);
        eventSort(day2List);
        eventSort(day3List);
        eventSort(day4List);

        RecyclerView recyclerViewDay1 =(RecyclerView)findViewById(R.id.category_day_1_recycler_view);
        if(day1List.isEmpty()){
            noEventsDay1.setVisibility(View.VISIBLE);
            recyclerViewDay1.setVisibility(View.GONE);
        }
        else{
            recyclerViewDay1.setAdapter(new CategoryEventsAdapter(day1List,this,getBaseContext()));
            recyclerViewDay1.setNestedScrollingEnabled(false);
            recyclerViewDay1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }

        RecyclerView recyclerViewDay2 = (RecyclerView)findViewById(R.id.category_day_2_recycler_view);
        if(day2List.isEmpty()){
            noEventsDay2.setVisibility(View.VISIBLE);
            recyclerViewDay2.setVisibility(View.GONE);
        }
        else{
            recyclerViewDay2.setAdapter(new CategoryEventsAdapter(day2List,this,getBaseContext()));
            recyclerViewDay2.setNestedScrollingEnabled(false);
            recyclerViewDay2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }

        RecyclerView recyclerViewDay3 = (RecyclerView)findViewById(R.id.category_day_3_recycler_view);
        if(day3List.isEmpty()){
            noEventsDay3.setVisibility(View.VISIBLE);
            recyclerViewDay3.setVisibility(View.GONE);
        }
        else {
            recyclerViewDay3.setAdapter(new CategoryEventsAdapter(day3List, this,getBaseContext()));
            recyclerViewDay3.setNestedScrollingEnabled(false);
            recyclerViewDay3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }

        RecyclerView recyclerViewDay4 = (RecyclerView)findViewById(R.id.category_day_4_recycler_view);
        if(day4List.isEmpty()){
            noEventsDay4.setVisibility(View.VISIBLE);
            recyclerViewDay4.setVisibility(View.GONE);
        }
        else {
            recyclerViewDay4.setAdapter(new CategoryEventsAdapter(day4List, this,getBaseContext()));
            recyclerViewDay4.setNestedScrollingEnabled(false);
            recyclerViewDay4.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
    }
    private void eventSort(List<EventModel> eventsList){
        Collections.sort(eventsList, new Comparator<EventModel>() {
            @Override
            public int compare(EventModel o1, EventModel o2) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.US);

                try {
                    Date d1 = sdf.parse(o1.getStartTime());
                    Date d2 = sdf.parse(o2.getStartTime());

                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(d1);
                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(d2);

                    long diff = c1.getTimeInMillis() - c2.getTimeInMillis();

                    if (diff>0) return 1;
                    else if (diff<0) return -1;
                    else{
                        int catDiff = o1.getCatName().compareTo(o2.getCatName());

                        if (catDiff>0) return 1;
                        else if (catDiff<0) return -1;
                        else {
                            int eventDiff = o1.getEventName().compareTo(o2.getEventName());

                            if (eventDiff>0) return 1;
                            else if (eventDiff<0) return -1;
                            else return 0;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mDatabase.close();
        mDatabase =null;
    }
}
