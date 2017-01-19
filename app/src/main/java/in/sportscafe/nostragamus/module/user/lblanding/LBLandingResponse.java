package in.sportscafe.nostragamus.module.user.lblanding;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.nostragamus.module.user.myprofile.myposition.dto.LbSummary;

/**
 * Created by Jeeva on 11/7/16.
 */
public class LBLandingResponse {

    @JsonProperty("data")
    private LBLandingSummary summary;

    @JsonProperty("data")
    public LBLandingSummary getSummary() {
        return summary;
    }

    @JsonProperty("data")
    public void setSummary(LBLandingSummary summary) {
        this.summary = summary;
    }

}