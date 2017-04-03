package in.sportscafe.nostragamus.module.allchallenges.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jeeva on 31/03/17.
 */
public class Pool {

    public interface DropDownIds {
        int NONE = 0;
        int MEMBER = 1;
        int REWARD = 2;
    }

    @JsonProperty("poll_id")
    private int pollId;

    @JsonProperty("poll_name")
    private String pollName;

    @JsonProperty("entry_fee")
    private int entryFee;

    @JsonProperty("member_details")
    private PoolMemberDetails memberDetails;

    @JsonProperty("reward_details")
    private PoolRewardDetails rewardDetails;

    @JsonIgnore
    private int dropDownId;

    @JsonProperty("poll_id")
    public int getPoolId() {
        return pollId;
    }

    @JsonProperty("poll_id")
    public void setPoolId(int pollId) {
        this.pollId = pollId;
    }

    @JsonProperty("poll_name")
    public String getPoolName() {
        return pollName;
    }

    @JsonProperty("poll_name")
    public void setPoolName(String pollName) {
        this.pollName = pollName;
    }

    @JsonProperty("entry_fee")
    public int getEntryFee() {
        return entryFee;
    }

    @JsonProperty("entry_fee")
    public void setEntryFee(int entryFee) {
        this.entryFee = entryFee;
    }

    @JsonProperty("member_details")
    public PoolMemberDetails getMemberDetails() {
        return memberDetails;
    }

    @JsonProperty("member_details")
    public void setMemberDetails(PoolMemberDetails memberDetails) {
        this.memberDetails = memberDetails;
    }

    @JsonProperty("reward_details")
    public PoolRewardDetails getRewardDetails() {
        return rewardDetails;
    }

    @JsonProperty("reward_details")
    public void setRewardDetails(PoolRewardDetails rewardDetails) {
        this.rewardDetails = rewardDetails;
    }

    @JsonIgnore
    public int getDropDownId() {
        return dropDownId;
    }

    @JsonIgnore
    public void setDropDownId(int dropDownId) {
        this.dropDownId = dropDownId;
    }
}