package in.sportscafe.nostragamus.module.challengeRewards.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 9/7/17.
 */

public class RewardsRequest {

    @SerializedName("contest_id")
    private int contestId;


    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

}
