package in.sportscafe.nostragamus.module.allchallenges.join.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 16/06/17.
 */

public class JoinChallengeRequest {

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("config_index")
    private int configIndex;

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public int getConfigIndex() {
        return configIndex;
    }

    public void setConfigIndex(int configIndex) {
        this.configIndex = configIndex;
    }
}
