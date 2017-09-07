package in.sportscafe.nostragamus.module.contest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 06/09/17.
 */

public class ContestEntriesRequest {

    @JsonProperty("contest_id")
    private Integer contestId;

    @JsonProperty("challenge_id")
    private Integer challengeId;

    public Integer getContestId() {
        return contestId;
    }

    public void setContestId(Integer contestId) {
        this.contestId = contestId;
    }

    public Integer getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Integer challengeId) {
        this.challengeId = challengeId;
    }
}
