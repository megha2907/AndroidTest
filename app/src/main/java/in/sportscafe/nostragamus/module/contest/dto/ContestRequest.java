package in.sportscafe.nostragamus.module.contest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 01/09/17.
 */

public class ContestRequest {

    @JsonProperty("challenge_id")
    private int challengeId;

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }
}
