package in.sportscafe.nostragamus.module.challengeRewards.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import in.sportscafe.nostragamus.module.contest.dto.bumper.BumperContestResponse;
import in.sportscafe.nostragamus.module.contest.dto.pool.PoolContestResponse;


/**
 * Created by deepanshi on 9/6/17.
 */

@Parcel
public class RewardsResponse {

    @SerializedName("contest_id")
    private int contestId;

    @SerializedName("challengeEndTime")
    private String challengeEndTime;

    @SerializedName("challengeStartTime")
    private String challengeStartTime;

    @SerializedName("users_joined")
    private int usersJoined;

    @SerializedName("max_participants")
    private int maxParticipants;

    @SerializedName("rewards")
    private List<Rewards> rewardsList = new ArrayList<>();

    @SerializedName("pool")
    private PoolContestResponse poolContestResponse;

    @SerializedName("bumper")
    private BumperContestResponse bumperContestResponse;

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

    public String getChallengeStartTime() {
        return challengeStartTime;
    }

    public void setChallengeStartTime(String challengeStartTime) {
        this.challengeStartTime = challengeStartTime;
    }

    public PoolContestResponse getPoolContestResponse() {
        return poolContestResponse;
    }

    public void setPoolContestResponse(PoolContestResponse poolContestResponse) {
        this.poolContestResponse = poolContestResponse;
    }

    public BumperContestResponse getBumperContestResponse() {
        return bumperContestResponse;
    }

    public void setBumperContestResponse(BumperContestResponse bumperContestResponse) {
        this.bumperContestResponse = bumperContestResponse;
    }

    public int getUsersJoined() {
        return usersJoined;
    }

    public void setUsersJoined(int usersJoined) {
        this.usersJoined = usersJoined;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }
}
