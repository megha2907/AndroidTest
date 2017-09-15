package in.sportscafe.nostragamus.module.user.leaderboard.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class LbGroup {

    @SerializedName("group_id")
    private Integer groupId;

    @SerializedName("sports")
    private List<LbSport> sports = new ArrayList<>();

    @SerializedName("group_id")
    public Integer getGroupId() {
        return groupId;
    }

    @SerializedName("group_id")
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @SerializedName("sports")
    public List<LbSport> getSports() {
        return sports;
    }

    @SerializedName("sports")
    public void setSports(List<LbSport> sports) {
        this.sports = sports;
    }
}