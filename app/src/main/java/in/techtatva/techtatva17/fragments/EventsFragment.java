package in.techtatva.techtatva17.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.adapters.EventsTabsPagerAdapter;
import in.techtatva.techtatva17.models.events.EventDetailsModel;
import in.techtatva.techtatva17.models.events.EventsListModel;
import in.techtatva.techtatva17.network.APIClient;
import io.realm.Realm;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {
    private List<EventDetailsModel> events = new ArrayList<>();
    Realm realm = Realm.getDefaultInstance();
    public EventsFragment() {
        // Required empty public constructor
    }


    public static EventsFragment newInstance() {
        EventsFragment fragment = new EventsFragment();
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.event_fragment);
        prepareData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_events, container, false);
        ViewPager viewPager = (ViewPager)result.findViewById(R.id.events_view_pager);
        TabLayout tabLayout = (TabLayout) result.findViewById(R.id.events_tab_layout);
        viewPager.setAdapter(new EventsTabsPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        return result;
    }
    private void prepareData(){
        APIClient.APIInterface api = APIClient.getAPIInterface();
        Call<EventsListModel> call = api.getEventsList();
        call.enqueue(new Callback<EventsListModel>() {
            @Override
            public void onResponse(Call<EventsListModel> call, Response<EventsListModel> response) {
                if(response.isSuccess()){
                    events = response.body().getEvents();
                    events.addAll(response.body().getEvents());
                    saveDataToRealm();
                }else{
                    Log.i("RESPONSE", "Bad response");
                }
            }
            @Override
            public void onFailure(Call<EventsListModel> call, Throwable t) {
                Log.i("RESPONSE", "No response");
            }
        });
    }
    private void saveDataToRealm(){

        try{
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if(events.size()!=0){
                        realm.copyToRealm(events);
                    }
                }
            });
        }catch (RealmPrimaryKeyConstraintException e){
            //Duplicate Record
            Log.i("REALM", "RealmPKConstraintException(Perhaps a duplicate record)"+e);
        }
    }

}
