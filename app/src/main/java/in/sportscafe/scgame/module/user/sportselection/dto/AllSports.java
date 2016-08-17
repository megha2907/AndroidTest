package in.sportscafe.scgame.module.user.sportselection.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jeeva.android.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class AllSports {

    @JsonProperty("data")
    private List<Sport> sports = new ArrayList<>();

    @JsonProperty("data")
    public List<Sport> getSports() {
        return sports;
    }

    @JsonProperty("data")
    public void setSports(List<Sport> sports) {
        this.sports = sports;
    }
}