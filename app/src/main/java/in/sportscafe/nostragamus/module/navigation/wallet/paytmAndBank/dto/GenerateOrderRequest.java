package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandip on 7/4/17.
 */

public class GenerateOrderRequest {

    @SerializedName("user_id")
    private long userId;

    @SerializedName("challenge_id")
    private long challengeId;

    @SerializedName("config_index")
    private int configIndex;

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

    public int getConfigIndex() {
        return configIndex;
    }

    public void setConfigIndex(int configIndex) {
        this.configIndex = configIndex;
    }
}
