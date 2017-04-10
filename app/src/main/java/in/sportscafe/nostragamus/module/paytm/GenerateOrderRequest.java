package in.sportscafe.nostragamus.module.paytm;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandip on 7/4/17.
 */

public class GenerateOrderRequest {

    @JsonProperty("USER_ID")
    private String userId;

    @JsonProperty("CHALLENGE_ID")
    private String challengeId;

    @JsonProperty("CONFIG_INDEX")
    private String configIndex;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public String getConfigIndex() {
        return configIndex;
    }

    public void setConfigIndex(String configIndex) {
        this.configIndex = configIndex;
    }
}
