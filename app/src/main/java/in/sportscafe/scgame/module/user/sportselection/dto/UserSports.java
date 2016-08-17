package in.sportscafe.scgame.module.user.sportselection.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class UserSports {

    @JsonProperty("data")
    private List<Integer> sports = new ArrayList<Integer>();

    @JsonProperty("data")
    public List<Integer> getSports() {
        return sports;
    }

    @JsonProperty("data")
    public void setSports(List<Integer> sports) {
        this.sports = sports;
    }
}