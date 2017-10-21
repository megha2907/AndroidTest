package in.sportscafe.nostragamus.module.challengeRewards.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by deepanshi on 9/6/17.
 */

@Parcel
public class RewardsResponse {

    @SerializedName("contest_id")
    private int contestId;

    @SerializedName("challengeEndTime")
    private String challengeEndTime;

    @SerializedName("rewards")
    private List<Rewards> rewardsList = new ArrayList<>();


    public String getChallengeEndTime() {
        return challengeEndTime;
    }

    public void setChallengeEndTime(String challengeEndTime) {
        this.challengeEndTime = challengeEndTime;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public List<Rewards> getRewardsList() {
        return rewardsList;
    }

    public void setRewardsList(List<Rewards> rewardsList) {
        this.rewardsList = rewardsList;
    }

}