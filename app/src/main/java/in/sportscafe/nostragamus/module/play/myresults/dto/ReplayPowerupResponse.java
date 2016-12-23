package in.sportscafe.nostragamus.module.play.myresults.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import in.sportscafe.nostragamus.module.play.prediction.dto.AudiencePoll;

/**
 * Created by deepanshi on 12/14/16.
 */

public class ReplayPowerupResponse {

    @JsonProperty("data")
    private String response;

    @JsonProperty("data")
    public String getResponse() {
        return response;
    }

    @JsonProperty("data")
    public void setResponse(String response) {
        this.response = response;
    }


}
