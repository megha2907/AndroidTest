package in.sportscafe.nostragamus.module.user.group.members;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeeva on 2/7/16.
 */
public class MembersRequest {

    @SerializedName("group_id")
    private Integer groupId;

    @SerializedName("player_id")
    private Integer playerId;

    @SerializedName("group_id")
    public Integer getGroupId() {
        return groupId;
    }

    @SerializedName("group_id")
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @SerializedName("player_id")
    public Integer getPlayerId() {
        return playerId;
    }

    @SerializedName("player_id")
    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }
}