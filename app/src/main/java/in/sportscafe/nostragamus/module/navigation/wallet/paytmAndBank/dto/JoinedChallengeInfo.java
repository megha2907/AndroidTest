package in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeUserInfo;

/**
 * Created by deepanshi on 4/12/17.
 */

@Parcel
public class JoinedChallengeInfo {

    @SerializedName("user_id")
    private int userId;

    @SerializedName("user_challenge_info")
    private ChallengeUserInfo challengeUserInfo;

    @SerializedName("fee")
    private Integer entryFee;

    @SerializedName("config_name")
    private String configName;

    @SerializedName("challenge_endtime")
    private String endTime;

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

    @SerializedName("fee")
    public Integer getEntryFee() {
        return entryFee;
    }

    @SerializedName("fee")
    public void setEntryFee(Integer entryFee) {
        this.entryFee = entryFee;
    }

    @SerializedName("config_name")
    public String getConfigName() {
        return configName;
    }

    @SerializedName("config_name")
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    @SerializedName("challenge_endtime")
    public String getEndTime() {
        return endTime;
    }

    @SerializedName("challenge_endtime")
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public boolean isFreeEntry() {
        return null == entryFee || entryFee == 0;
    }

}
