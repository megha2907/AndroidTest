package in.sportscafe.nostragamus.module.play.myresults.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by deepanshi on 12/14/16.
 */

public class ReplayPowerupResponse {

    @SerializedName("data")
    private String response;

    @SerializedName("data")
    public String getResponse() {
        return response;
    }

    @SerializedName("data")
    public void setResponse(String response) {
        this.response = response;
    }


}
