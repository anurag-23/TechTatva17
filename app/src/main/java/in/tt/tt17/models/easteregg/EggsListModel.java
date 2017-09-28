package in.tt.tt17.models.easteregg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bennyhawk on 27/9/17.
 */

public class EggsListModel {

    private List<EasterEggModel> eggs = new ArrayList<>();

     public EggsListModel(){

     }

    public List<EasterEggModel> getEggs() {
        return eggs;
    }

    public void setEggs(List<EasterEggModel> eggs) {
        this.eggs = eggs;
    }


}
