package in.tt.tt17.network;

import in.tt.tt17.models.categories.CategoriesListModel;
import in.tt.tt17.models.easteregg.EasterEggModel;
import in.tt.tt17.models.events.EventsListModel;
import in.tt.tt17.models.events.ScheduleListModel;
import in.tt.tt17.models.result.ResultsListModel;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Sapta on 5/28/2017.
 */

public class APIClient {

    private static Retrofit retrofit = null;
    private static Retrofit retrofitEgg = null;

    private static final String BASE_URL = "";
    private static final String EGGS_URL = "http://qnaxzrzrf.herokuapp.com/api/";

    public static APIInterface getAPIInterface(){

        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            //retrofitEgg = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        }

        return retrofit.create(APIInterface.class);
    }

    public static APIInterface getEggAPIInterface(){

        if (retrofitEgg == null){
            //retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            retrofitEgg = new Retrofit.Builder().baseUrl(EGGS_URL).addConverterFactory(GsonConverterFactory.create()).build();

        }

        return retrofitEgg.create(APIInterface.class);
    }

    public interface APIInterface{
        @GET("events")
        Call<EventsListModel> getEventsList();

        @GET("categories")
        Call<CategoriesListModel> getCategoriesList();

        @GET("results")
        Call<ResultsListModel> getResultsList();

        @GET("schedule")
        Call<ScheduleListModel> getScheduleList();

        @GET("photos")
        Call<EasterEggModel[]> getEggList();

    }
}