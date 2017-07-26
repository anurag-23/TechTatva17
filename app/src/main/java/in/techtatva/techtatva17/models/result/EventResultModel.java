package in.techtatva.techtatva17.models.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skvrahul on 15/7/17.
 */
//Moved to separate class from ResultsFragment
public class EventResultModel {
    public String eventName;
    public String eventRound;
    public String eventCategory;
    public List<ResultModel> eventResultsList = new ArrayList<>();
}
