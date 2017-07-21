package in.sportscafe.nostragamus.module.play.powerup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 21/07/17.
 */

public class PowerupBankStatusRequest {

    @JsonProperty("challenge_id")
    private int challengeId;

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }
}
