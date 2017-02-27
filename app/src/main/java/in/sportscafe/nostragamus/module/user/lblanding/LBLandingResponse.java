package in.sportscafe.nostragamus.module.user.lblanding;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 11/7/16.
 */
public class LBLandingResponse {

    @JsonProperty("data")
    private List<LBLandingSummary> summary= new ArrayList<>();

    @JsonProperty("data")
    public List<LBLandingSummary> getSummary() {
        return summary;
    }

    @JsonProperty("data")
    public void setSummary(List<LBLandingSummary> summary) {
        this.summary = summary;
    }

}