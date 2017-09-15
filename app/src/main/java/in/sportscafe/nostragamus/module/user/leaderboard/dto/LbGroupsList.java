package in.sportscafe.nostragamus.module.user.leaderboard.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeeva on 30/6/16.
 */
public class LbGroupsList {

    @SerializedName("groups")
    private List<LbGroup> groups = new ArrayList<>();

    @SerializedName("data")
    public List<LbGroup> getGroups() {
        return groups;
    }

    @SerializedName("data")
    public void setGroups(List<LbGroup> groups) {
        this.groups = groups;
    }
}