package in.techtatva.techtatva17.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
        getActivity().setTitle(R.string.developers_fragment);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().findViewById(R.id.toolbar).setElevation(0);
                AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar);
                appBarLayout.setElevation(0);
                appBarLayout.setTargetElevation(0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_developers, container, false);
    }


}
