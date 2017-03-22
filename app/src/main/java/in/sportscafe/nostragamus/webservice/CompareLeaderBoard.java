package in.sportscafe.nostragamus.webservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

/**
 * Created by deepanshi on 2/14/17.
 */
@Parcel
public class CompareLeaderBoard {

    @JsonProperty("user2")
    private String userId;

    @JsonProperty("user2_rank")
    private Integer userRank;

    @JsonProperty("user1")
    private String playerId;

    @JsonProperty("user1_rank")
    private Integer playerRank;

    @JsonProperty("name")
    private String Name; // challenge Name or Group Name

    @JsonProperty("type")
    private String type; // challenge or group

    @JsonProperty("user2")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("user2")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("user2_rank")
    public Integer getUserRank() {
        return userRank;
    }

    @JsonProperty("user2_rank")
    public void setUserRank(Integer userRank) {
        this.userRank = userRank;
    }

    @JsonProperty("user1")
    public String getPlayerId() {
        return playerId;
    }

    @JsonProperty("user1")
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    @JsonProperty("user1_rank")
    public Integer getPlayerRank() {
        return playerRank;
    }

    @JsonProperty("user1_rank")
    public void setPlayerRank(Integer playerRank) {
        this.playerRank = playerRank;
    }

    @JsonProperty("name")
    public String getName() {
        return Name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        Name = name;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }
}
