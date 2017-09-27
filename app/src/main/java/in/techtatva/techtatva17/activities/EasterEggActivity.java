package in.techtatva.techtatva17.activities;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.List;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.adapters.EasterEggAdapter;
import in.techtatva.techtatva17.models.easteregg.EasterEggModel;
import in.techtatva.techtatva17.models.easteregg.EggsListModel;
import in.techtatva.techtatva17.models.events.ScheduleListModel;
import in.techtatva.techtatva17.network.APIClient;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EasterEggActivity extends AppCompatActivity {

    RecyclerView eggsRecycler;
    String [] eggList = new String[500];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easter_egg);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.parseColor("#0d0d0d"));
            getWindow().setNavigationBarColor(Color.parseColor("#0d0d0d"));

        }




        eggsRecycler = (RecyclerView) findViewById(R.id.eggs_recycler);
        loadEggsFromInternet();






    }
    private void displayEggs(){

        eggsRecycler.setAdapter(new EasterEggAdapter(eggList));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        eggsRecycler.setLayoutManager(gridLayoutManager);
    }

    private void loadEggsFromInternet() {
        Call<EasterEggModel[]> call = APIClient.getEggAPIInterface().getEggList();
        call.enqueue(new Callback<EasterEggModel[]>() {
            @Override
            public void onResponse(Call<EasterEggModel[]> call, Response<EasterEggModel[]> response) {

                if (response.isSuccess() && response.body() != null){

                    int i = 0;
                    for(EasterEggModel temp:response.body()){
                        eggList[i] = temp.getEggSourceImg();
                        i++;
                    }
                    displayEggs();
                }

            }

            @Override
            public void onFailure(Call<EasterEggModel[]> call, Throwable t) {
                Log.d("sfsdsdfsdf",t.getMessage());
            }
        });
    }
}
