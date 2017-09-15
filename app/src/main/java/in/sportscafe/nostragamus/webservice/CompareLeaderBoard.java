package in.sportscafe.nostragamus.webservice;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 2/14/17.
 */
@Parcel
public class CompareLeaderBoard {

    @SerializedName("user1")
    private String userId;

    @SerializedName("user1_rank")
    private Integer userRank;

    @SerializedName("user2")
    private String playerId;

    @SerializedName("user2_rank")
    private Integer playerRank;

    @SerializedName("name")
    private String Name; // challenge Name or Group Name

    @SerializedName("type")
    private String type; // challenge or group

    @SerializedName("user1")
    public String getUserId() {
        return userId;
    }

    @SerializedName("user1")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @SerializedName("user1_rank")
    public Integer getUserRank() {
        return userRank;
    }

    @SerializedName("user1_rank")
    public void setUserRank(Integer userRank) {
        this.userRank = userRank;
    }

    @SerializedName("user2")
    public String getPlayerId() {
        return playerId;
    }

    @SerializedName("user2")
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @SerializedName("user2_rank")
    public Integer getPlayerRank() {
        return playerRank;
    }

    @SerializedName("user2_rank")
    public void setPlayerRank(Integer playerRank) {
        this.playerRank = playerRank;
    }

    @SerializedName("name")
    public String getName() {
        return Name;
    }

    @SerializedName("name")
    public void setName(String name) {
        Name = name;
    }

    @SerializedName("type")
    public String getType() {
        return type;
    }

    @SerializedName("type")
    public void setType(String type) {
        this.type = type;
    }
}
