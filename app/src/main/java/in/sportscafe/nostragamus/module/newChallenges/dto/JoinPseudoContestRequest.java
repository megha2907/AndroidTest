package in.sportscafe.nostragamus.module.newChallenges.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 23/09/17.
 */

public class JoinPseudoContestRequest {

    @SerializedName("challenge_id")
    private int challengeId;

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }
}
