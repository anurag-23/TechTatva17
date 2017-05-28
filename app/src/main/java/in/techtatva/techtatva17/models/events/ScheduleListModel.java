package in.techtatva.techtatva17.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sapta on 5/27/2017.
 */

public class ScheduleListModel {

    @SerializedName("data")
    @Expose
    private List<ScheduleModel> data = new ArrayList<ScheduleModel>();

    public List<ScheduleModel> getData() {
        return data;
    }

    public void setData(List<ScheduleModel> data) {
        this.data = data;
    }

}