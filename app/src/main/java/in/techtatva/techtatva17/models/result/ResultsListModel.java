package in.techtatva.techtatva17.models.result;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sapta on 5/27/2017.
 */

public class ResultsListModel {
    @SerializedName("data")
    @Expose
    private List<ResultModel> data = new ArrayList<ResultModel>();

    public List<ResultModel> getData() {
        return data;
    }

    public void setData(List<ResultModel> data) {
        this.data = data;
    }

}