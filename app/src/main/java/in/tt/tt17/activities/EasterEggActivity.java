package in.tt.tt17.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import in.tt.tt17.R;
import in.tt.tt17.adapters.EasterEggAdapter;
import in.tt.tt17.models.easteregg.EasterEggModel;
import in.tt.tt17.network.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EasterEggActivity extends AppCompatActivity {

    private RecyclerView eggsRecycler;
    private String [] eggList = new String[500];
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView noConnection;

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
        progressBar = (ProgressBar) findViewById(R.id.easter_egg_progress_bar);
        noConnection = (TextView) findViewById(R.id.easter_egg_no_connection);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        loadEggsFromInternet();
    }

    private void displayEggs(){

        eggsRecycler.setAdapter(new EasterEggAdapter(eggList));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        eggsRecycler.setLayoutManager(staggeredGridLayoutManager);
        eggsRecycler.setVisibility(View.VISIBLE);
    }

    private void loadEggsFromInternet() {
        progressBar.setVisibility(View.VISIBLE);
        eggsRecycler.setVisibility(View.GONE);
        noConnection.setVisibility(View.GONE);

        Call<EasterEggModel[]> call = APIClient.getEggAPIInterface().getEggList();
        call.enqueue(new Callback<EasterEggModel[]>() {
            @Override
            public void onResponse(Call<EasterEggModel[]> call, Response<EasterEggModel[]> response) {
                progressBar.setVisibility(View.GONE);
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
                noConnection.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
