package in.sportscafe.nostragamus.module.challengeRules.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 9/15/17.
 */

@Parcel
public class RulesRequest {

    @JsonProperty("contest_id")
    private int contestId;

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

}
