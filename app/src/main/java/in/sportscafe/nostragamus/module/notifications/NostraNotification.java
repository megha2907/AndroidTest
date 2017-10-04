package in.sportscafe.nostragamus.module.notifications;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sandip on 03/10/17.
 */
@Parcel
public class NostraNotification {

    @SerializedName("screenName")
    private String screenName;

    @SerializedName("screenData")
    private NostraNotificationData data;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public NostraNotificationData getData() {
        return data;
    }

    public void setData(NostraNotificationData data) {
        this.data = data;
    }
}
