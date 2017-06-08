package in.techtatva.techtatva17.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.adapters.EventsTabsPagerAdapter;
import in.techtatva.techtatva17.application.TechTatva;
import in.techtatva.techtatva17.models.events.EventDetailsModel;
import in.techtatva.techtatva17.models.events.EventsListModel;
import in.techtatva.techtatva17.models.events.ScheduleListModel;
import in.techtatva.techtatva17.models.events.ScheduleModel;
import in.techtatva.techtatva17.network.APIClient;
import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {
    private List<EventDetailsModel> events = new ArrayList<>();
    Realm mDatabase = Realm.getDefaultInstance();
    View result;
    ViewPager viewPager;
    private MenuItem searchItem;



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
        mDatabase.close();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.event_fragment);
        setHasOptionsMenu(true);
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().findViewById(R.id.toolbar).setElevation(0);
                AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
                appBarLayout.setElevation(0);
                appBarLayout.setTargetElevation(0);
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        prepareData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        result = inflater.inflate(R.layout.fragment_events, container, false);
        viewPager = (ViewPager)result.findViewById(R.id.events_view_pager);
        TabLayout tabLayout = (TabLayout) result.findViewById(R.id.events_tab_layout);
        viewPager.setAdapter(new EventsTabsPagerAdapter(getChildFragmentManager(),""));

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
                    Log.i("RESPONSE", "Reached here");

                    viewPager.setAdapter(new EventsTabsPagerAdapter(getChildFragmentManager(),""));  //add this line to refresh layout

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
            mDatabase.executeTransaction(new Realm.Transaction() {
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_hardware, menu);
        searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView)searchItem.getActionView();

        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));


        searchView.setSubmitButtonEnabled(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String text) {

                viewPager.getAdapter().notifyDataSetChanged();



                viewPager.setAdapter(new EventsTabsPagerAdapter(getChildFragmentManager(),text));
                viewPager.getAdapter().notifyDataSetChanged();




                TechTatva.searchOpen = 2;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {

                viewPager.getAdapter().notifyDataSetChanged();

                viewPager.setAdapter(new EventsTabsPagerAdapter(getChildFragmentManager(),text));
                viewPager.getAdapter().notifyDataSetChanged();


                TechTatva.searchOpen = 2;
                return false;
            }
        });
        searchView.setQueryHint("Search Events");




        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                viewPager.setAdapter(new EventsTabsPagerAdapter(getChildFragmentManager(),""));
                searchView.clearFocus();

                TechTatva.searchOpen = 2;
                return false;
            }


        });
    }



}
