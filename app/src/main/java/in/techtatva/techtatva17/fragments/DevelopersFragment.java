package in.techtatva.techtatva17.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.techtatva.techtatva17.R;


public class DevelopersFragment extends Fragment {


    public DevelopersFragment() {
        // Required empty public constructor
    }

    public static DevelopersFragment newInstance() {
        DevelopersFragment fragment = new DevelopersFragment();
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
        return inflater.inflate(R.layout.fragment_developers, container, false);
    }

}
