package in.sportscafe.nostragamus.module.popups.banktransfer;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeUserInfo;

/**
 * Created by Jeeva on 02/03/17.
 */
public class BankTransfer {

    @JsonProperty("challenge_id")
    private int challengeId;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("user_challenge_info")
    private ChallengeUserInfo challengeUserInfo;

    @JsonProperty("challenge_id")
    public int getChallengeId() {
        return challengeId;
    }

    @JsonProperty("challenge_id")
    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    @JsonProperty("user_id")
    public int getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @JsonProperty("user_challenge_info")
    public ChallengeUserInfo getChallengeUserInfo() {
        return challengeUserInfo;
    }

    @JsonProperty("user_challenge_info")
    public void setChallengeUserInfo(ChallengeUserInfo challengeUserInfo) {
        this.challengeUserInfo = challengeUserInfo;
    }
}