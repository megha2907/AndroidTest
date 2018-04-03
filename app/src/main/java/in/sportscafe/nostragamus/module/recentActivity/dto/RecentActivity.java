package in.sportscafe.nostragamus.module.recentActivity.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import in.sportscafe.nostragamus.module.notifications.NostraNotification;

/**
 * Created by deepanshi on 3/22/18.
 */

@Parcel
public class RecentActivity {

    @SerializedName("text")
    private String activityText;

    @SerializedName("type")
    private String activityType;

    @SerializedName("is_unread")
    private boolean isUnread;

    @SerializedName("time")
    private String activityTime;

    @SerializedName("info")
    private NostraNotification nostraRecentActivityInfo;

    public String getActivityText() {
        return activityText;
    }

    public void setActivityText(String activityText) {
        this.activityText = activityText;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public boolean isUnread() {
        return isUnread;
    }

    public void setUnread(boolean unread) {
        isUnread = unread;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public NostraNotification getNostraRecentActivityInfo() {
        return nostraRecentActivityInfo;
    }

    public void setNostraRecentActivityInfo(NostraNotification nostraRecentActivityInfo) {
        this.nostraRecentActivityInfo = nostraRecentActivityInfo;
    }

}
