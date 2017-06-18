package in.techtatva.techtatva17.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.adapters.ResultsAdapter;
import in.techtatva.techtatva17.models.result.ResultModel;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class ResultsFragment extends Fragment {

    Realm mDatabase;
    private List<EventResultModel> resultsList = new ArrayList<>();
    private ResultsAdapter adapter;

    public ResultsFragment() {
        // Required empty public constructor
    }

    public static ResultsFragment newInstance() {
        ResultsFragment fragment = new ResultsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.results_fragment);
        mDatabase = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_results, container, false);

        RecyclerView resultsRecyclerView = (RecyclerView)view.findViewById(R.id.results_recycler_view);
        adapter = new ResultsAdapter(resultsList, getActivity());
        resultsRecyclerView.setAdapter(adapter);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(resultsRecyclerView.getContext(),DividerItemDecoration.VERTICAL);
        resultsRecyclerView.addItemDecoration(dividerItemDecoration);

        displayData();
        return view;
    }


    private void displayData(){
        if (mDatabase == null) return;

        RealmResults<ResultModel> results = mDatabase.where(ResultModel.class).findAllSorted("eventName", Sort.ASCENDING, "position", Sort.ASCENDING);

        if (!results.isEmpty()){
            resultsList.clear();

            List<String> eventNamesList = new ArrayList<>();

            for (ResultModel result : results){
                String eventName = result.getEventName()+" "+result.getRound();
                if (eventNamesList.contains(eventName)){
                    resultsList.get(eventNamesList.indexOf(eventName)).eventResultsList.add(result);
                }
                else{
                    EventResultModel eventResult = new EventResultModel();
                    eventResult.eventName = result.getEventName();
                    eventResult.eventRound = result.getRound();
                    eventResult.eventCategory = result.getCatName();
                    eventResult.eventResultsList.add(result);
                    resultsList.add(eventResult);
                    eventNamesList.add(eventName);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    public class EventResultModel {
        public String eventName;
        public String eventRound;
        public String eventCategory;
        public List<ResultModel> eventResultsList = new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabase.close();
        mDatabase = null;
    }

}
