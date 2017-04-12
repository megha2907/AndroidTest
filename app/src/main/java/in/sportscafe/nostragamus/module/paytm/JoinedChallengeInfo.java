package in.sportscafe.nostragamus.module.paytm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.allchallenges.dto.ChallengeUserInfo;

/**
 * Created by deepanshi on 4/12/17.
 */

@Parcel
public class JoinedChallengeInfo {

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("user_challenge_info")
    private ChallengeUserInfo challengeUserInfo;

    @JsonProperty("fee")
    private Integer entryFee;

    @JsonProperty("config_name")
    private String configName;

    @JsonProperty("challenge_endtime")
    private String endTime;

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

    @JsonProperty("fee")
    public Integer getEntryFee() {
        return entryFee;
    }

    @JsonProperty("fee")
    public void setEntryFee(Integer entryFee) {
        this.entryFee = entryFee;
    }

    @JsonProperty("config_name")
    public String getConfigName() {
        return configName;
    }

    @JsonProperty("config_name")
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    @JsonProperty("challenge_endtime")
    public String getEndTime() {
        return endTime;
    }

    @JsonProperty("challenge_endtime")
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @JsonIgnore
    public boolean isFreeEntry() {
        return null == entryFee || entryFee == 0;
    }

}
