package in.sportscafe.nostragamus.module.user.leaderboard.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeeva on 30/6/16.
 */
public class GroupsLbResponse {

    @SerializedName("data")
    private LbGroupsList lbGroupsList;

    @SerializedName("data")
    public LbGroupsList getLbGroupsList() {
        return lbGroupsList;
    }

    @SerializedName("data")
    public void setLbGroupsList(LbGroupsList lbGroupsList) {
        this.lbGroupsList = lbGroupsList;
    }
}