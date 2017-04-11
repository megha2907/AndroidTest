package in.sportscafe.nostragamus.module.paytm;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 7/4/17.
 */

public class GenerateOrderRequest {

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("challenge_id")
    private long challengeId;

    @JsonProperty("config_index")
    private long configIndex;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(long challengeId) {
        this.challengeId = challengeId;
    }

    public long getConfigIndex() {
        return configIndex;
    }

    public void setConfigIndex(long configIndex) {
        this.configIndex = configIndex;
    }
}
