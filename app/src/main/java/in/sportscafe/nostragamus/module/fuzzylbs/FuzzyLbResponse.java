package in.sportscafe.nostragamus.module.fuzzylbs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.user.lblanding.LbLanding;

/**
 * Created by Jeeva on 10/01/17.
 */

public class FuzzyLbResponse {

    @JsonProperty("data")
    private List<LbLanding> lbs = new ArrayList<>();

    @JsonProperty("data")
    public List<LbLanding> getLbs() {
        return lbs;
    }

    @JsonProperty("data")
    public void setLbs(List<LbLanding> lbs) {
        this.lbs = lbs;
    }
}