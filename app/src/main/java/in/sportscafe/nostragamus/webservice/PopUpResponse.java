package in.sportscafe.nostragamus.webservice;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.popups.PopUp;

/**
 * Created by deepanshi on 2/2/17.
 */
public class PopUpResponse {

    @SerializedName("data")
    private List<PopUp> popUps = new ArrayList<>();

    @SerializedName("data")
    public List<PopUp> getPopUps() {
        return popUps;
    }

    @SerializedName("data")
    public void setPopUps(List<PopUp> popUps) {
        this.popUps = popUps;
    }
}
