package in.techtatva.techtatva17.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.models.categories.CategoriesListModel;
import in.techtatva.techtatva17.models.categories.CategoryModel;
import in.techtatva.techtatva17.models.events.EventDetailsModel;
import in.techtatva.techtatva17.models.events.EventsListModel;
import in.techtatva.techtatva17.models.events.ScheduleListModel;
import in.techtatva.techtatva17.models.events.ScheduleModel;
import in.techtatva.techtatva17.models.result.ResultModel;
import in.techtatva.techtatva17.models.result.ResultsListModel;
import in.techtatva.techtatva17.network.APIClient;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    boolean isWiFi;
    boolean isConnected;

    public Runnable test;

    private Realm mDatabase;

    private int counter = 0;

    private Context context = this;

    private Handler mHandler = new Handler();

    private boolean eventsDataAvailableLocally = false;
    private boolean schedulesDataAvailableLocally = false;
    private boolean categoriesDataAvailableLocally = false;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mDatabase = Realm.getDefaultInstance();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean dataAvailableLocally = sharedPref.getBoolean("dataAvailableLocally",false);



        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected){
            isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        }

        if (dataAvailableLocally){
            if(isConnected && isWiFi){
                Toast.makeText(context, "Updating data", Toast.LENGTH_SHORT).show();
                loadAllFromInternet();
            }
            else{
                loadResultsFromInternet(); //Updating every time on reload
                moveForward();
            }
        }

        else{

            if (!isConnected){
                Toast.makeText(context, "Please check your network settings and reload app", Toast.LENGTH_SHORT).show();
            }

            else{
                Toast.makeText(context, "Loading data for the first time. Takes a couple of seconds", Toast.LENGTH_SHORT).show();
                loadAllFromInternet();
            }



        }
    }

    private void loadAllFromInternet(){
        loadEventsFromInternet();
        loadSchedulesFromInternet();
        loadCategoriesFromInternet();
        loadResultsFromInternet();

        test = new Runnable() {
            @Override
            public void run() {

                if (eventsDataAvailableLocally && schedulesDataAvailableLocally && categoriesDataAvailableLocally){
                    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("dataAvailableLocally",true);
                    editor.apply();
                    moveForward();
                }

                if (!(eventsDataAvailableLocally && schedulesDataAvailableLocally && categoriesDataAvailableLocally)){
                    counter++;

                    if (counter == 10){
                        if(eventsDataAvailableLocally || schedulesDataAvailableLocally || categoriesDataAvailableLocally){
                            Toast.makeText(context, "Possible slow internet connection", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, "Server may be down. Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                    mHandler.postDelayed(test,1000);

                }

            }
        };

        mHandler.postDelayed(test,1000);
    }

    private void moveForward(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void loadEventsFromInternet() {

        Call<EventsListModel> eventsCall = APIClient.getAPIInterface().getEventsList();

        eventsCall.enqueue(new Callback<EventsListModel>() {

            @Override
            public void onResponse(Call<EventsListModel> call, Response<EventsListModel> response) {
                if (response.isSuccess() && response.body() != null && mDatabase != null) {

                    mDatabase.beginTransaction();
                    mDatabase.where(EventDetailsModel.class).findAll().deleteAllFromRealm();
                    mDatabase.copyToRealm(response.body().getEvents());
                    mDatabase.commitTransaction();
                    eventsDataAvailableLocally=true;
                    Log.d("TAG","Events");
                }
            }

            @Override
            public void onFailure(Call<EventsListModel> call, Throwable t) {

            }
        });
    }

    private void loadSchedulesFromInternet() {

        Call<ScheduleListModel> schedulesCall = APIClient.getAPIInterface().getScheduleList();

        schedulesCall.enqueue(new Callback<ScheduleListModel>() {

            @Override
            public void onResponse(Call<ScheduleListModel> call, Response<ScheduleListModel> response) {
                if (response.isSuccess() && response.body() != null && mDatabase != null) {

                    mDatabase.beginTransaction();
                    mDatabase.where(ScheduleModel.class).findAll().deleteAllFromRealm();
                    mDatabase.copyToRealm(response.body().getData());
                    mDatabase.commitTransaction();
                    schedulesDataAvailableLocally=true;
                    Log.d("TAG","Schedules");
                }
            }

            @Override
            public void onFailure(Call<ScheduleListModel> call, Throwable t) {

            }
        });
    }

    private void loadCategoriesFromInternet() {

        Call<CategoriesListModel> categoriesCall = APIClient.getAPIInterface().getCategoriesList();

        categoriesCall.enqueue(new Callback<CategoriesListModel>() {

            @Override
            public void onResponse(Call<CategoriesListModel> call, Response<CategoriesListModel> response) {
                if (response.isSuccess() && response.body() != null && mDatabase != null) {

                    mDatabase.beginTransaction();
                    mDatabase.where(CategoryModel.class).findAll().deleteAllFromRealm();
                    mDatabase.copyToRealm(response.body().getCategoriesList());
                    mDatabase.where(CategoryModel.class).equalTo("categoryName", "minimilitia").or().equalTo("categoryName", "Mini Militia").or().equalTo("categoryName", "Minimilitia").or().equalTo("categoryName", "MiniMilitia").or().equalTo("categoryName", "MINIMILITIA").or().equalTo("categoryName", "MINI MILITIA").findAll().deleteAllFromRealm();
                    mDatabase.commitTransaction();
                    categoriesDataAvailableLocally=true;
                    Log.d("TAG","Categories");
                }
            }

            @Override
            public void onFailure(Call<CategoriesListModel> call, Throwable t) {

            }
        });
    }

    private void loadResultsFromInternet(){

        Call<ResultsListModel> resultsCall = APIClient.getAPIInterface().getResultsList();

        resultsCall.enqueue(new Callback<ResultsListModel>() {

            @Override
            public void onResponse(Call<ResultsListModel> call, Response<ResultsListModel> response) {
                if (response.isSuccess() && response.body() != null && mDatabase != null) {

                    mDatabase.beginTransaction();
                    mDatabase.where(ResultModel.class).findAll().deleteAllFromRealm();
                    mDatabase.copyToRealm(response.body().getData());
                    mDatabase.commitTransaction();
                    Log.d("TAG","Results");
                }
            }

            @Override
            public void onFailure(Call<ResultsListModel> call, Throwable t) {

            }
        });
    }
/*
    @Override
    public void onBackPressed(){
        super.onBackPressed();

        if(eventsCall.isExecuted()||schedulesCall.isExecuted()||categoriesCall.isExecuted()){
            eventsCall.cancel();
            schedulesCall.cancel();
            categoriesCall.cancel();
        }
    }
*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabase.close();
    }


}
