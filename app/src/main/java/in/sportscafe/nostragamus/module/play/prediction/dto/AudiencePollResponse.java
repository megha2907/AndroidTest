package in.sportscafe.nostragamus.module.play.prediction.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by deepanshi on 11/24/16.
 */

public class AudiencePollResponse {

    @SerializedName("data")
    private List<AudiencePoll> audiencePoll;

    @SerializedName("data")
    public List<AudiencePoll> getAudiencePoll() {
        return audiencePoll;
    }

    @SerializedName("data")
    public void setAudiencePoll(List<AudiencePoll> audiencePoll) {
        this.audiencePoll = audiencePoll;
    }

}