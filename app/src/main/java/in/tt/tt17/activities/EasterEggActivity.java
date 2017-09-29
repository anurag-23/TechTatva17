package in.tt.tt17.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import in.tt.tt17.R;
import in.tt.tt17.adapters.EasterEggAdapter;
import in.tt.tt17.models.easteregg.EasterEggModel;
import in.tt.tt17.network.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EasterEggActivity extends AppCompatActivity {

    RecyclerView eggsRecycler;
    String [] eggList = new String[500];
    Toolbar toolbar;

//    @Override
//    public boolean onNavigateUp() {
//        finish();
//        return true;
//    }
//
//    pu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easter_egg);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.parseColor("#0d0d0d"));
            getWindow().setNavigationBarColor(Color.parseColor("#0d0d0d"));

        }
        eggsRecycler = (RecyclerView) findViewById(R.id.eggs_recycler);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        loadEggsFromInternet();
    }

    private void displayEggs(){

        eggsRecycler.setAdapter(new EasterEggAdapter(eggList));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        eggsRecycler.setLayoutManager(staggeredGridLayoutManager);
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
