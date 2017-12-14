package in.sportscafe.nostragamus.module.contest.dto.pool;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by sc on 7/12/17.
 */
@Parcel
public class PoolContestResponse {

    @SerializedName("min_participants")
    private int minParticipants;

    @SerializedName("max_participants")
    private int maxParticipants;

    @SerializedName("per_user_prize")
    private double prizePerUser;

    @SerializedName("step")
    private String roundingLevel;

    @SerializedName("users_joined")
    private int usersJoined;

    @SerializedName("payout_map")
    private List<PoolPayoutMap> poolPayoutMapList;

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

    public String getRoundingLevel() {
        return roundingLevel;
    }

    public void setRoundingLevel(String roundingLevel) {
        this.roundingLevel = roundingLevel;
    }

    public List<PoolPayoutMap> getPoolPayoutMapList() {
        return poolPayoutMapList;
    }

    public void setPoolPayoutMapList(List<PoolPayoutMap> poolPayoutMapList) {
        this.poolPayoutMapList = poolPayoutMapList;
    }

    public int getUsersJoined() {
        return usersJoined;
    }

    public void setUsersJoined(int usersJoined) {
        this.usersJoined = usersJoined;
    }

}
