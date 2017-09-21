package in.sportscafe.nostragamus.module.prediction.powerupBank.dto;

import com.google.gson.annotations.SerializedName;

import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeUserInfo;

public class BankTransfer {

    @SerializedName("challenge_id")
    private int challengeId;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("user_challenge_info")
    private ChallengeUserInfo challengeUserInfo;

    @SerializedName("challenge_id")
    public int getChallengeId() {
        return challengeId;
    }

    @SerializedName("challenge_id")
    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    @SerializedName("user_id")
    public int getUserId() {
        return userId;
    }

    @SerializedName("user_id")
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @SerializedName("user_challenge_info")
    public ChallengeUserInfo getChallengeUserInfo() {
        return challengeUserInfo;
    }

    @SerializedName("user_challenge_info")
    public void setChallengeUserInfo(ChallengeUserInfo challengeUserInfo) {
        this.challengeUserInfo = challengeUserInfo;
    }
}