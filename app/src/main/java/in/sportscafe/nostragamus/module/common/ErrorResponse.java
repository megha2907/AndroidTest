package in.sportscafe.nostragamus.module.common;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sc on 6/11/17.
 */

public class ErrorResponse {

    @SerializedName("error")
    private String error;

    @SerializedName("error_code")
    private int errorCode = 0;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
