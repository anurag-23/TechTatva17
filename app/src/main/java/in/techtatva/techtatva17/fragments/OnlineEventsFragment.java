package in.techtatva.techtatva17.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.techtatva.techtatva17.R;


public class OnlineEventsFragment extends Fragment {



    public OnlineEventsFragment() {
        // Required empty public constructor
    }


    public static OnlineEventsFragment newInstance() {
        OnlineEventsFragment fragment = new OnlineEventsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_online_events, container, false);
    }

}
