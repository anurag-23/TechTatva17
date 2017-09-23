package in.techtatva.techtatva17.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.activities.MainActivity;
import in.techtatva.techtatva17.adapters.HomeAdapter;
import in.techtatva.techtatva17.adapters.HomeCategoriesAdapter;
import in.techtatva.techtatva17.adapters.HomeFavouritesAdapter;
import in.techtatva.techtatva17.adapters.HomeResultsAdapter;
import in.techtatva.techtatva17.models.categories.CategoryModel;
import in.techtatva.techtatva17.models.events.ScheduleModel;
import in.techtatva.techtatva17.models.favourites.FavouritesModel;
import in.techtatva.techtatva17.models.instagram.InstagramFeed;
import in.techtatva.techtatva17.models.result.EventResultModel;
import in.techtatva.techtatva17.models.result.ResultModel;
import in.techtatva.techtatva17.models.result.ResultsListModel;
import in.techtatva.techtatva17.network.APIClient;
import in.techtatva.techtatva17.network.InstaFeedAPIClient;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private InstagramFeed feed;
//    private ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;
    private HomeAdapter instaAdapter;
    private HomeResultsAdapter resultsAdapter;
    private HomeCategoriesAdapter categoriesAdapter;
    private HomeFavouritesAdapter favouritesAdapter;
    private RecyclerView homeRV;
    private RecyclerView resultsRV;
    private RecyclerView  categoriesRV;
    private RecyclerView favouritesRV;
    private TextView resultsMore;
    private TextView categoriesMore;
    private TextView favouritesMore;
    private TextView resultsNone;
    private CardView homeResultsItem;
    private ProgressDialog progressDialog;
    private BottomNavigationView navigation;
    private AppBarLayout appBarLayout;
    private TextView instaTextView;
    private boolean ANIMATED_PROGRESS_DIALOG = false;
    private int processes = 0;
    String TAG = "HomeFragment";
    Realm mDatabase = Realm.getDefaultInstance();
    private List<EventResultModel> resultsList = new ArrayList<>();
    private List<CategoryModel> categoriesList = new ArrayList<>();
    private List<ScheduleModel> eventsList = new ArrayList<>();
    public HomeFragment() {
        // Required empty public constructor
    }



    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.app_name);
        fetchResults();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and initialize Views
        final View view = initViews(inflater, container);
        //Progress Dialog
        /*if(ANIMATED_PROGRESS_DIALOG){
            progressDialogAnimation.setVisibility(View.VISIBLE);
            ((View)progressDialogAnimation.getParent()).setVisibility(View.VISIBLE);
            progressDialogAnimation.smoothToShow();
        }else{
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Loading....");
            progressDialog.show();
        }*/



        //Fetch and Display InstaFeed
        displayInstaFeed();

        //Fetch and display Results


        //updateResultsList();


        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected){
            //dismissDialog();
        }

        resultsAdapter = new HomeResultsAdapter(resultsList,getActivity());
        resultsRV.setAdapter(resultsAdapter);
        resultsRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        resultsMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MORE Clicked - Take user to Results Fragment
                Log.i(TAG, "onClick: Results more");
                ((MainActivity)getActivity()).changeFragment(ResultsFragment.newInstance());
            }
        });

        //Display Categories
        RealmResults<CategoryModel> categoriesRealmList = mDatabase.where(CategoryModel.class).findAllSorted("categoryName");
        categoriesList = mDatabase.copyFromRealm(categoriesRealmList);
        if(categoriesList.size()>10){
            categoriesList.subList(10,categoriesList.size()).clear();
        }
        categoriesAdapter = new HomeCategoriesAdapter(categoriesList,getActivity());
        categoriesRV.setAdapter(categoriesAdapter);
        categoriesRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        categoriesAdapter.notifyDataSetChanged();
        categoriesMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MORE Clicked - Take user to Categories Fragment
                Log.i(TAG, "onClick: Categories More");
                ((MainActivity)getActivity()).changeFragment(CategoriesFragment.newInstance());

            }
        });
        if(categoriesList.size()==0){
            view.findViewById(R.id.home_categories_none_text_view).setVisibility(View.VISIBLE);
        }

        //Display favourites
        RealmResults<ScheduleModel> eventsRealmResults = mDatabase.where(ScheduleModel.class).findAllSorted("day", Sort.ASCENDING, "startTime", Sort.ASCENDING);
        eventsList = mDatabase.copyFromRealm(eventsRealmResults);
        for(int i=0;i<eventsList.size();i++){
            ScheduleModel event = eventsList.get(i);
            if(isFavourite(event)){
                //Move to top
                eventsList.remove(event);
                eventsList.add(0, event);
            }
        }
        if(eventsList.size()>10){
            eventsList.subList(10, eventsList.size()).clear();
        }
        favouritesAdapter = new HomeFavouritesAdapter(eventsList, null,getActivity());
        Log.i(TAG, "onCreateView: FavouritesList size"+eventsList.size());
        favouritesRV.setAdapter(favouritesAdapter);
        favouritesRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        favouritesAdapter.notifyDataSetChanged();
        favouritesMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MORE Clicked - Take user to Favourites Fragment
                Log.i(TAG, "onClick: Favourites More");
                ((MainActivity)getActivity()).changeFragment(EventsFragment.newInstance());
            }
        });
        if(eventsList.size()==0){
            view.findViewById(R.id.home_favourites_none_text_view).setVisibility(View.VISIBLE);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConnectivityManager cmTemp = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkTemp = cmTemp.getActiveNetworkInfo();
                boolean isConnectedTemp = activeNetworkTemp != null && activeNetworkTemp.isConnectedOrConnecting();
                if(isConnectedTemp){
                    displayInstaFeed();
                    fetchResults();
                    new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }, 5000);
                }
                else{
                    Snackbar.make(view, "Check connection!", Snackbar.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);}

            }
        });

        return view;
    }



    public void displayInstaFeed(){
        Call<InstagramFeed> call = InstaFeedAPIClient.getInterface().getInstagramFeed();
        processes ++;
        call.enqueue(new Callback<InstagramFeed>() {
            @Override
            public void onResponse(Call<InstagramFeed> call, Response<InstagramFeed> response) {
                if(response.isSuccess()){
                    feed = response.body();
                    instaAdapter =  new HomeAdapter(feed);
                    homeRV.setAdapter(instaAdapter);
                    homeRV.setLayoutManager(new LinearLayoutManager(getContext()));
                    ViewCompat.setNestedScrollingEnabled(homeRV, false);

                    //dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<InstagramFeed> call, Throwable t) {
                //dismissDialog();
                Log.i(TAG, "onFailure: Error Fetching insta feed ");
            }
        });
    }

    public void updateResultsList(){

        RealmResults<ResultModel> results = mDatabase.where(ResultModel.class).findAllSorted("eventName", Sort.ASCENDING, "teamID",Sort.ASCENDING );
        if (!results.isEmpty()){
            resultsList.clear();
            List<String> eventNamesList = new ArrayList<>();
            for (ResultModel result : results){
                String eventName = result.getEventName()+" "+result.getRound();
                if (eventNamesList.contains(eventName)){
                    resultsList.get(eventNamesList.indexOf(eventName)).eventResultsList.add(result);
                }
                else{
                    EventResultModel eventResult = new EventResultModel();
                    eventResult.eventName = result.getEventName();
                    eventResult.eventRound = result.getRound();
                    eventResult.eventCategory = result.getCatName();
                    eventResult.eventResultsList.add(result);
                    resultsList.add(eventResult);
                    eventNamesList.add(eventName);
                }
            }
        }
        Log.i(TAG, "displayResults: resultsList size:"+resultsList.size());
        if(resultsList.size()>10){
            resultsList.subList(10,resultsList.size()).clear();
        }
        resultsAdapter.notifyDataSetChanged();

        if(resultsList.size()==0){
            /*if(homeResultsItem.getVisibility()==View.GONE)
                homeResultsItem.setVisibility(View.VISIBLE);*/
            resultsNone.setVisibility(View.VISIBLE);
        }

    }
    public void fetchResults(){
        processes++;
        Call<ResultsListModel> callResultsList = APIClient.getAPIInterface().getResultsList();
        callResultsList.enqueue(new Callback<ResultsListModel>() {
            List<ResultModel> results = new ArrayList<ResultModel>();
            @Override
            public void onResponse(Call<ResultsListModel> call, Response<ResultsListModel> response) {
                if (response.isSuccess() && response.body() != null){
                    results = response.body().getData();
                    mDatabase.beginTransaction();
                    mDatabase.where(ResultModel.class).findAll().deleteAllFromRealm();
                    mDatabase.copyToRealm(results);
                    mDatabase.commitTransaction();

                    homeResultsItem.setVisibility(View.VISIBLE);
                    updateResultsList();
                    resultsNone.setVisibility(View.GONE);
                    resultsNone.setText("");
                    //dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<ResultsListModel> call, Throwable t) {
                //Snackbar.make(rootLayout, "Loading data. Takes a couple of seconds", Snackbar.LENGTH_SHORT).show();
                if(homeResultsItem.getVisibility()==View.VISIBLE)
                homeResultsItem.setVisibility(View.GONE);
                /*resultsNone.setVisibility(View.VISIBLE);
                resultsNone.setText("Error fetching results");*/
                //Toast.makeText(getContext(), "Error fetching results", Toast.LENGTH_SHORT).show();
                processes--;
                //dismissDialog();
            }
        });
    }
    public boolean isFavourite(ScheduleModel event){
        RealmResults<FavouritesModel> favouritesRealmList = mDatabase.where(FavouritesModel.class).equalTo("id",event.getEventID()).contains("day", event.getDay()).findAll();
        return (favouritesRealmList.size()!=0);
    }


    //Functions common to all sections
    public void dismissDialog(){
        processes--;
        Log.i(TAG, "dismissDialog: Processes"+processes);

        if(processes == 0){
//            if(ANIMATED_PROGRESS_DIALOG){
//                progressDialogAnimation.smoothToHide();
//            }else{
//                progressDialog.dismiss();
//            }
//            }
        }
    }
    public View initViews(LayoutInflater inflater, ViewGroup container){
        appBarLayout = (AppBarLayout) container.findViewById(R.id.app_bar);
        navigation = (BottomNavigationView) container.findViewById(R.id.bottom_nav);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeRV = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        resultsRV = (RecyclerView) view.findViewById(R.id.home_results_recycler_view);
        categoriesRV = (RecyclerView) view.findViewById(R.id.home_categories_recycler_view);
        favouritesRV = (RecyclerView) view.findViewById(R.id.home_favourites_recycler_view);
        resultsMore = (TextView) view.findViewById(R.id.home_results_more_text_view);
        categoriesMore = (TextView) view.findViewById(R.id.home_categories_more_text_view);
        favouritesMore = (TextView) view.findViewById(R.id.home_favourites_more_text_view);
        resultsNone = (TextView) view.findViewById(R.id.home_results_none_text_view);
        homeResultsItem=(CardView) view.findViewById(R.id.home_results_item);
        instaTextView = (TextView) view.findViewById(R.id.instagram_textview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_swipe_refresh_layout);
        return view;
    }
}
