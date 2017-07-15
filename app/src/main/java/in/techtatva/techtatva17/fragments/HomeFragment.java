package in.techtatva.techtatva17.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.adapters.HomeAdapter;
import in.techtatva.techtatva17.models.instagram.InstagramFeed;
import in.techtatva.techtatva17.network.InstaFeedAPIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private InstagramFeed feed;
    private HomeAdapter adapter;
    private RecyclerView homeRV;
    String TAG = "HomeFragment";
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
        loadFeed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeRV = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        return view;
    }
    public void loadFeed(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        Call<InstagramFeed> call = InstaFeedAPIClient.getInterface().getInstagramFeed();
        call.enqueue(new Callback<InstagramFeed>() {
            @Override
            public void onResponse(Call<InstagramFeed> call, Response<InstagramFeed> response) {
                if(response.isSuccess()){
                    feed = response.body();
                    progressDialog.dismiss();
                    adapter =  new HomeAdapter(feed);
                    homeRV.setAdapter(adapter);
                    homeRV.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onFailure(Call<InstagramFeed> call, Throwable t) {
                    progressDialog.dismiss();
                Log.i(TAG, "onFailure: Error Fetching insta feed ");
            }
        });
    }
}
