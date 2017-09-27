package in.techtatva.techtatva17.models.easteregg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;

/**
 * Created by bennyhawk on 27/9/17.
 */

public class EasterEggModel implements RealmModel {

    @SerializedName("id")
    @Expose
    private String eggID;

    @SerializedName("link")
    @Expose
    private String eggFBLink;

    @SerializedName("source")
    @Expose
    private String eggSourceImg;

    public EasterEggModel(){

    }

    public String getEggFBLink() {
        return eggFBLink;
    }

    public String getEggID() {
        return eggID;
    }

    public String getEggSourceImg() {
        return eggSourceImg;
    }
}
