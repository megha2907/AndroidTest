package in.sportscafe.nostragamus.webservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.popups.PopUp;

/**
 * Created by deepanshi on 2/2/17.
 */
public class PopUpResponse {

    @JsonProperty("data")
    private List<PopUp> popUps = new ArrayList<>();

    @JsonProperty("data")
    public List<PopUp> getPopUps() {
        return popUps;
    }

    @JsonProperty("data")
    public void setPopUps(List<PopUp> popUps) {
        this.popUps = popUps;
    }
}
