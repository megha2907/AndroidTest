package in.sportscafe.nostragamus.module.common;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jeeva on 30/6/16.
 */
public class ApiResponse {

    @SerializedName("data")
    private String message;

    @SerializedName("data")
    public String getMessage() {
        return message;
    }

    @SerializedName("data")
    public void setMessage(String message) {
        this.message = message;
    }
}