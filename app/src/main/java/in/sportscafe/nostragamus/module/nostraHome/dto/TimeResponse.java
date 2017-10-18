package in.sportscafe.nostragamus.module.nostraHome.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 5/6/17.
 */

public class TimeResponse {

    @SerializedName("timestamp")
    private String serverTime;

    @SerializedName("timestamp")
    public String getServerTime() {
        return serverTime;
    }

    @SerializedName("timestamp")
    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
}
