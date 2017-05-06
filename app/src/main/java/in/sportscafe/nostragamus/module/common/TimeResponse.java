package in.sportscafe.nostragamus.module.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by deepanshi on 5/6/17.
 */

public class TimeResponse {

    @JsonProperty("timestamp")
    private String serverTime;

    @JsonProperty("timestamp")
    public String getServerTime() {
        return serverTime;
    }

    @JsonProperty("timestamp")
    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }
}
