package in.sportscafe.nostragamus.module.play.prediction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by deepanshi on 11/24/16.
 */

public class AudiencePollResponse {

    @JsonProperty("data")
    private List<AudiencePoll> audiencePoll;

    @JsonProperty("data")
    public List<AudiencePoll> getAudiencePoll() {
        return audiencePoll;
    }

    @JsonProperty("data")
    public void setAudiencePoll(List<AudiencePoll> audiencePoll) {
        this.audiencePoll = audiencePoll;
    }

}