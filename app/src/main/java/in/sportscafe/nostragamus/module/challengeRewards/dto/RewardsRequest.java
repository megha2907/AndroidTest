package in.sportscafe.nostragamus.module.challengeRewards.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 9/7/17.
 */

public class RewardsRequest {

    @JsonProperty("contest_id")
    private int contestId;


    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

}
