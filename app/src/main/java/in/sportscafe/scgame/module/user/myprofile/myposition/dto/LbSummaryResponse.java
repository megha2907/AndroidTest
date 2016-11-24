package in.sportscafe.scgame.module.user.myprofile.myposition.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 11/7/16.
 */
public class LbSummaryResponse {

    @JsonProperty("data")
    private LbSummary lbSummary;

    @JsonProperty("data")
    public LbSummary getLbSummary() {
        return lbSummary;
    }

    @JsonProperty("data")
    public void setLbSummary(LbSummary lbSummary) {
        this.lbSummary = lbSummary;
    }

}