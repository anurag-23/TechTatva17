package in.tt.tt17.network;

import in.tt.tt17.models.instagram.InstagramFeed;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by skvrahul on 13/7/17.
 */

public class InstaFeedAPIClient {

    public static final String BASE_URL_INSTAGRAM="https://api.instagram.com/";
    private static Retrofit retrofit = null;

    public static APIInterface getInterface(){

        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL_INSTAGRAM).addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit.create(APIInterface.class);
    }

    public interface APIInterface {
        //Query TAG is set 'techtatva17' for this year's app
        //TODO: Change search TAG accordingly
        @GET("")
        Call<InstagramFeed> getInstagramFeed();

    }
}
