package in.sportscafe.nostragamus.module.contest.dto.bumper;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

import in.sportscafe.nostragamus.module.contest.dto.pool.PoolPayoutMap;

/**
 * Created by deepanshi on 2/2/18.
 */
@Parcel
public class BumperContestResponse {

    @SerializedName("min_participants")
    private int minParticipants;

    @SerializedName("max_participants")
    private int maxParticipants;

    @SerializedName("per_user_prize")
    private double prizePerUser;

    @SerializedName("users_joined")
    private int usersJoined;

    @SerializedName("bumper_rewards")
    private List<BumperRewards> bumperRewardsList;

    public int getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public double getPrizePerUser() {
        return prizePerUser;
    }

    public void setPrizePerUser(double prizePerUser) {
        this.prizePerUser = prizePerUser;
    }

    public int getUsersJoined() {
        return usersJoined;
    }

    public void setUsersJoined(int usersJoined) {
        this.usersJoined = usersJoined;
    }

    public List<BumperRewards> getBumperRewardsList() {
        return bumperRewardsList;
    }

    public void setBumperRewardsList(List<BumperRewards> bumperRewardsList) {
        this.bumperRewardsList = bumperRewardsList;
    }

}
