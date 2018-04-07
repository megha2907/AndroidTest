package in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sc on 27/3/18.
 */
@Parcel
public class FindPrivateContestResponse {

    @SerializedName("error")
    private String error;

    @SerializedName("error_code")
    private int errorCode = 0;

    @SerializedName("data")
    private FindPrivateContestResponseData data;

    public FindPrivateContestResponseData getData() {
        return data;
    }

    public void setData(FindPrivateContestResponseData data) {
        this.data = data;
    }

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
