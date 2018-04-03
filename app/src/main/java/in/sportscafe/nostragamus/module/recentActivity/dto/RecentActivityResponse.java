package in.sportscafe.nostragamus.module.recentActivity.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by deepanshi on 3/22/18.
 */

public class RecentActivityResponse {

    @SerializedName("data")
    List<RecentActivity> recentActivityList;

    public List<RecentActivity> getRecentActivityList() {
        return recentActivityList;
    }

    public void setRecentActivityList(List<RecentActivity> recentActivityList) {
        this.recentActivityList = recentActivityList;
    }
}
