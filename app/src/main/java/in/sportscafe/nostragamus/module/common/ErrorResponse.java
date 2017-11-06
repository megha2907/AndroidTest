package in.sportscafe.nostragamus.module.common;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 6/11/17.
 */

public class ErrorResponse {

    @SerializedName("error")
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
